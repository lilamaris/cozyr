package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

public interface ReservableScheduleReader {
    ReservableSeatSchedule findBySeat(LocalDate targetDate, SeatLocator seatLocator);

    Optional<Instant> findLatestReservedEndAtByRoomId(RoomId roomId, Instant asOf, ZoneId timezone);
}