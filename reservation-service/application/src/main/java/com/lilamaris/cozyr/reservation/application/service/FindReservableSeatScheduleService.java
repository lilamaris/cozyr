package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.port.in.FindReservableSeatScheduleUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservableSeatScheduleQuery;
import com.lilamaris.cozyr.reservation.application.port.out.ReservableScheduleReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservableSeatScheduleService implements FindReservableSeatScheduleUseCase {
    private final ReservableScheduleReader reader;
    private final SeatReader seatReader;

    @Override
    public ReservableSeatSchedule find(FindReservableSeatScheduleQuery query) {
        var targetDate = query.targetDate();
        var seatId = query.seatId();

        var seatExists = seatReader.existsById(seatId);
        if (!seatExists) throw new ApplicationException(ReservationServiceProgressCode.SEAT_NOT_FOUND);

        return reader.findBySeat(targetDate, seatId);
    }
}