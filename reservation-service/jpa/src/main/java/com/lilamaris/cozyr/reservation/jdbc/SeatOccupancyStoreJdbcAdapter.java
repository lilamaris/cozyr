package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.domain.ReservationId;
import com.lilamaris.cozyr.reservation.jdbc.sql.SeatOccupancySql;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SeatOccupancyStoreJdbcAdapter implements SeatOccupancyStore {
    private final JdbcClient jdbcClient;

    @Override
    public boolean tryOccupy(ReservationId reservationId, LocalDate occupancyDate, SeatLocator seatLocator, Set<UUID> scheduleSlotIds) {
        var sql = SeatOccupancySql.INSERT_BY_SCHEDULE_SLOT_IDS;

        UUID[] slotIds = scheduleSlotIds.toArray(UUID[]::new);

        try {
            int rowCount = jdbcClient.sql(sql)
                    .param("roomId", seatLocator.roomId().getValue())
                    .param("seatId", seatLocator.seatId().getValue())
                    .param("reservationId", reservationId.getValue())
                    .param("occupancyDate", occupancyDate)
                    .param("scheduleSlotIds", slotIds)
                    .update();

            return rowCount == scheduleSlotIds.size();
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    @Override
    public boolean tryRelease(ReservationId reservationId, Instant releasedAt) {
        var sql = SeatOccupancySql.RELEASE_BY_RESERVATION_ID;

        int rowCount = jdbcClient.sql(sql)
                    .param("reservationId", reservationId.getValue())
                .param("releasedAt", Timestamp.from(releasedAt))
                    .update();

        return rowCount > 0;
    }
}
