package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.time.Instant;
import java.util.UUID;

public interface ReservationStatusStore {
    boolean cancel(ReservationId reservationId, Instant canceledAt);
}
