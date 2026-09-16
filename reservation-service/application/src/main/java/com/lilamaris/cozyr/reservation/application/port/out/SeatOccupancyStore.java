package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface SeatOccupancyStore {
    boolean tryOccupy(ReservationId reservationId, LocalDate occupancyDate, SeatLocator seatLocator, Set<UUID> scheduleSlotIds);

    boolean tryRelease(ReservationId reservationId, Instant releasedAt);
}