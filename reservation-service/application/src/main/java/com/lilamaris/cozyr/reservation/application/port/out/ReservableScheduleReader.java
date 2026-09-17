package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

import java.time.LocalDate;

public interface ReservableScheduleReader {
    ReservableSeatSchedule findBySeat(LocalDate targetDate, SeatLocator seatLocator);
}