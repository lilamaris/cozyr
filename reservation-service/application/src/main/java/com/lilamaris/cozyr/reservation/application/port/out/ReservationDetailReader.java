package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;

import java.util.Optional;
import java.util.UUID;

public interface ReservationDetailReader {
    Optional<ReservationDetail> find(UUID reservationId);
}
