package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.internal.ReservationInternalService;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ReserveSeatService implements ReserveSeatUseCase {
    private final ReservationInternalService reservationInternalService;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public ReserveSeatResult reserve(ReserveSeatCommand command) {
        var now = clock.instant();
        var reservation = reservationInternalService.reserve(
                command.seatLocator(),
                command.reserveDate(),
                command.scheduleSlotIds(),
                command.reserveUserId(),
                now
        );

        var event = reservation.toEvent();
        messagePublisher.publish(event.toMessage(now));

        return reservation;
    }
}
