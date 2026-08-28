package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationCursor;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationFilter;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationSummary;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationSummaryReader;
import com.lilamaris.cozyr.reservation.jpa.row.ReservationRow;
import com.lilamaris.cozyr.reservation.jpa.sql.ReservationSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ReservationSummaryReaderJdbcAdapter implements ReservationSummaryReader {
    private final JdbcClient jdbcClient;

    @Override
    public CursorResult<ReservationSummary, ReservationCursor> find(ReservationFilter filter, CursorRequest<ReservationCursor> request) {
        var conditions = new ArrayList<String>();
        var params = new MapSqlParameterSource()
                .addValue("limit", request.size() + 1);

        appendFilter(conditions, params, filter);
        appendCursor(conditions, params, request.cursor());

        var dynamicWhere = conditions.isEmpty()
                ? ""
                : "AND " + String.join("\nAND ", conditions);

        var sql = ReservationSql.LIST_SUMMARIES.formatted(dynamicWhere);

        var rows = jdbcClient.sql(sql)
                .paramSource(params)
                .query(ReservationRow.Summary.class)
                .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream()
                .limit(request.size())
                .filter(Objects::nonNull)
                .map(ReservationRow.Summary::toSummary)
                .toList();

        ReservationCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> ReservationCursor.of(last.createdAt(), last.reservationId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }

    private void appendFilter(List<String> conditions, MapSqlParameterSource params, ReservationFilter filter) {
        Optional.ofNullable(filter.reservedUserId()).ifPresent(reservedUserId -> {
            conditions.add("r.reserved_user_id = :reservedUserId");
            params.addValue("reservedUserId", reservedUserId);
        });

        Optional.ofNullable(filter.statuses())
                .filter(Predicate.not(Set::isEmpty))
                .ifPresent(statuses -> {
                    conditions.add("r.status IN (:statuses)");
                    params.addValue(
                            "statuses",
                            statuses.stream().map(Enum::name).toList()
                    );
                });

        Optional.ofNullable(filter.roomId())
                .ifPresent(roomId -> {
                    conditions.add("""
                            EXISTS (
                                SELECT 1
                                FROM seat_occupancy o
                                WHERE o.reservation_id = r.id
                                    AND o.room_id = :roomId
                            )
                            """);
                    params.addValue("roomId", roomId);
                });

        Optional.ofNullable(filter.seatId())
                .ifPresent(seatId -> {
                    conditions.add("""
                            EXISTS(
                                SELECT 1
                                FROM seat_occupancy o
                                WHERE o.reservation_id = r.id
                                    AND o.seat_id = :seatId
                            )
                            """);
                    params.addValue("seatId", seatId);
                });
    }

    private void appendCursor(List<String> conditions, MapSqlParameterSource params, @Nullable ReservationCursor cursor) {
        if (cursor == null) return;

        conditions.add("(r.created_at, r.id) < (:cursorCreatedAt, :cursorId)");
        params.addValue("cursorCreatedAt", cursor.createdAt());
        params.addValue("cursorId", cursor.reservationId());
    }
}
