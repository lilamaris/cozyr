package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.jpa.sql.SeatOccupancySql;
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
public class SeatOccupancyStoreJpaAdapter implements SeatOccupancyStore {
    private final JdbcClient jdbcClient;

    @Override
    public boolean tryOccupy(UUID reservationId, LocalDate occupancyDate, SeatId seatId, Set<UUID> scheduleSlotIds) {
        var sql = SeatOccupancySql.INSERT_BY_SCHEDULE_SLOT_IDS;

        UUID[] slotIds = scheduleSlotIds.toArray(UUID[]::new);

        try {
            int rowCount = jdbcClient.sql(sql)
                    .param("reservationId", reservationId)
                    .param("occupancyDate", occupancyDate)
                    .param("roomId", seatId.getRoomId())
                    .param("seatId", seatId.getSeatId())
                    .param("scheduleSlotIds", slotIds)
                    .update();

            return rowCount == scheduleSlotIds.size();
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    @Override
    public boolean tryRelease(UUID reservationId, Instant releasedAt) {
        var sql = SeatOccupancySql.RELEASE_BY_RESERVATION_ID;

        int rowCount = jdbcClient.sql(sql)
                    .param("reservationId", reservationId)
                .param("releasedAt", Timestamp.from(releasedAt))
                    .update();

        return rowCount > 0;
    }
}
