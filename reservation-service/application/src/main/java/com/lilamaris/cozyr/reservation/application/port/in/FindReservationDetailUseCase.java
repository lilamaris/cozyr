package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservationDetailQuery;

public interface FindReservationDetailUseCase {
    ReservationDetail find(FindReservationDetailQuery query);
}
