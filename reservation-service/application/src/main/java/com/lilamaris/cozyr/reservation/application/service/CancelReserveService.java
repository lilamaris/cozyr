package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.port.in.CancelReserveUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CancelReserveCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.CancelReserveResult;
import com.lilamaris.cozyr.reservation.application.port.out.DailyUsageCounter;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationContextReader;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationStatusStore;
import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.contract.event.ReservationCanceledEvent;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CancelReserveService implements CancelReserveUseCase {
    private final ReservationContextReader reservationContextReader;
    private final ReservationStatusStore reservationStatusStore;
    private final SeatOccupancyStore seatOccupancyStore;
    private final DailyUsageCounter dailyUsageCounter;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public CancelReserveResult cancel(CancelReserveCommand command) {
        var reservationId = command.reservationId();
        var context = reservationContextReader.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.RESERVATION_NOT_FOUND));

        var now = clock.instant();
        var isCanceled = reservationStatusStore.cancel(reservationId, now);
        if (!isCanceled)
            throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_ALREADY_CANCELED);

        var isReleased = seatOccupancyStore.tryRelease(reservationId, now);
        if (!isReleased)
            throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_ALREADY_CANCELED);

        var counterAcquired = dailyUsageCounter.tryDecrease(context.reservedUser().userId(), context.seatId().getRoomId(), context.reservationDate());
        if (!counterAcquired)
            throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_ALREADY_CANCELED);

        var schedules = context.schedules().stream()
                .map(RoomSchedule::toLocalTimeSchedule)
                .toList();
        var event = ReservationCanceledEvent.of(reservationId, context.reservationDate(), context.seatId().getRoomId(), context.seatId().getSeatId(), context.reservedUser().userId(), schedules);
        messagePublisher.publish(event.toMessage(now));

        return CancelReserveResult.of(reservationId, now);
    }
}
