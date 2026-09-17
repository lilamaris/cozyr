package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.util.Optional;

public interface ReservationDetailReader {
    Optional<ReservationDetail> find(ReservationId reservationId);
}
