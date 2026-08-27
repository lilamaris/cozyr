package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.ReservationStatusStore;
import com.lilamaris.cozyr.reservation.jpa.sql.ReservationSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationStatusStoreJdbcAdapter implements ReservationStatusStore {
    private final JdbcClient jdbcClient;

    @Override
    public boolean cancel(UUID reservationId, Instant canceledAt) {
        var sql = ReservationSql.CANCEL_BY_ID;

        int rowCount = jdbcClient.sql(sql)
                .param("reservationId", reservationId)
                .param("canceledAt", Timestamp.from(canceledAt))
                .update();

        return rowCount > 0;
    }
}