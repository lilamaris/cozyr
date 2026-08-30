package com.lilamaris.cozyr.reservation.application.port.out;

import java.time.Instant;
import java.util.UUID;

public interface ReservationStatusStore {
    boolean cancel(UUID reservationId, Instant canceledAt);
}
