package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationDetailReader;
import com.lilamaris.cozyr.reservation.jpa.row.ReservationRow;
import com.lilamaris.cozyr.reservation.jpa.sql.ReservationSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationDetailReaderJpaAdapter implements ReservationDetailReader {
    private final JdbcClient jdbcClient;

    @Override
    public Optional<ReservationDetail> find(UUID reservationId) {
        var sql = ReservationSql.FIND_DETAIL_BY_ID;

        var rows = jdbcClient.sql(sql)
                .param("reservationId", reservationId)
                .query(ReservationRow.Detail.class)
                .list();

        if (rows.isEmpty()) return Optional.empty();

        var first = rows.getFirst();

        if (first == null) return Optional.empty();

        var schedules = rows.stream()
                .filter(Objects::nonNull)
                .map(ReservationRow.Detail::toRoomSchedule)
                .toList();

        return Optional.of(
                ReservationDetail.of(
                        first.reservationId(),
                        first.toSeatId(),
                        first.status(),
                        schedules,
                        first.createdAt(),
                        first.updatedAt(),
                        first.toUserProjection()
                )
        );
    }
}
