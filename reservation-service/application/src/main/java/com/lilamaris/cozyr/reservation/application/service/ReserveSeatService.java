package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ReserveSeatService implements ReserveSeatUseCase {
    private final ReservationStore reservationStore;

    private final SeatReader seatReader;
    private final RoomScheduleSlotReader roomScheduleSlotReader;
    private final SeatOccupancyStore seatOccupancyStore;
    private final Clock clock;

    @Override
    @Transactional
    public ReserveSeatResult reserve(ReserveSeatCommand command) {
        var reserveSeatId = command.reserveSeatId();
        var slotIds = command.scheduleSlotIds();

        if (slotIds.isEmpty()) throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_NOT_FOUND);

        var slotIdsExistsInRoom = roomScheduleSlotReader.existsByRoom(reserveSeatId.getRoomId(), slotIds);
        if (!slotIdsExistsInRoom) throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_NOT_FOUND);

        var seatExists = seatReader.existsById(reserveSeatId);
        if (!seatExists) throw new ApplicationException(ReservationServiceProgressCode.SEAT_NOT_FOUND);

        var now = clock.instant();
        var reserveUserId = command.reserveUserId();
        var reserveDate = command.reserveDate();
        var reservation = Reservation.of(reserveUserId, reserveSeatId, reserveDate, now);

        var saved = reservationStore.save(reservation);
        var occupied = seatOccupancyStore.tryOccupy(saved.getId(), reserveDate, reserveSeatId, slotIds);
        if (!occupied) throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_ALREADY_OCCUPIED);

        return ReserveSeatResult.from(saved);
    }
}
