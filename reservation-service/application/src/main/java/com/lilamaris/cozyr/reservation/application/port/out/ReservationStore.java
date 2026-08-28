package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Reservation;

public interface ReservationStore {
    Reservation save(Reservation reservation);
}
