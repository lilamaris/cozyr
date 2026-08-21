package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.model.room.RoomFilter;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomRepository;
import com.lilamaris.cozyr.reservation.jpa.row.RoomSummaryRow;
import com.lilamaris.cozyr.reservation.jpa.sql.RoomSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomReaderJpaAdapter implements RoomReader {
    private final RoomRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Room> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public CursorResult<RoomSummary, RoomCursor> findSummaries(RoomFilter filter, CursorRequest<RoomCursor> request) {
        var conditions = new ArrayList<String>();
        var params = new MapSqlParameterSource()
                .addValue("limit", request.size() + 1);

        appendFilterCondition(conditions, params, filter);
        appendCursorCondition(conditions, params, request.cursor());

        var dynamicWhere = conditions.isEmpty()
                ? ""
                : "AND " + String.join("\nAND ", conditions);

        var sql = RoomSql.LIST_SUMMARIES.formatted(dynamicWhere);

        var rows = jdbcClient.sql(sql)
                .paramSource(params)
                .query(RoomSummaryRow.class)
                .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream()
                .limit(request.size())
                .filter(Objects::nonNull)
                .map(RoomSummaryRow::toSummary)
                .toList();

        RoomCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> RoomCursor.of(last.createdAt(), last.roomId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }

    @Override
    public Optional<RoomDetail> findDetailById(long id) {
        var sql = RoomSql.FIND_DETAIL_BY_ID;

        return jdbcClient.sql(sql)
                .param("roomId", id)
                .query(RoomDetail.class)
                .optional();
    }

    private void appendFilterCondition(
            List<String> conditions,
            MapSqlParameterSource params,
            RoomFilter filter
    ) {
        Optional.ofNullable(filter.name()).ifPresent(name -> {
            conditions.add("r.name ILIKE :name");
            params.addValue("name", "%" + escapeLike(name) + "%");
        });

        Optional.ofNullable(filter.description()).ifPresent(description -> {
            conditions.add("r.description ILIKE :description");
            params.addValue("description", "%" + escapeLike(description) + "%");
        });
    }

    private void appendCursorCondition(
            List<String> conditions,
            MapSqlParameterSource params,
            @Nullable RoomCursor cursor
    ) {
        if (cursor == null) return;

        conditions.add("(r.created_at, r.id) < (:cursorCreatedAt, :cursorId)");
        params.addValue("cursorCreatedAt", cursor.createdAt());
        params.addValue("cursorId", cursor.roomId());
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
