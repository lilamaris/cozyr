package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface SeatOccupancyStore {
    boolean tryOccupy(UUID reservationId, LocalDate occupancyDate, SeatId seatId, Set<UUID> scheduleSlotIds);

    boolean tryRelease(UUID reservationId, Instant releasedAt);
}