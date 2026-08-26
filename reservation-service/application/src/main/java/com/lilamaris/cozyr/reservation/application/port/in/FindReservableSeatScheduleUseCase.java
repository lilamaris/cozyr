package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservableSeatScheduleQuery;

public interface FindReservableSeatScheduleUseCase {
    ReservableSeatSchedule find(FindReservableSeatScheduleQuery query);
}
