package com.lilamaris.cozyr.reservation.application.port.out;

import java.util.UUID;

public interface ReservationReader {
    boolean existsById(UUID reservationId);
}
