package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.in.CreateSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.SeatCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatStore;
import com.lilamaris.cozyr.reservation.contract.event.SeatCreatedEvent;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateSeatService implements CreateSeatUseCase {
    private final SeatReader reader;
    private final SeatStore store;

    private final RoomReader roomReader;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public SeatCreatedResult create(CreateSeatCommand command) {
        var id = command.id();

        var roomId = id.getRoomId();
        var roomExists = roomReader.existsById(roomId);
        if (!roomExists) throw new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND);

        var exists = reader.existsById(id);
        if (exists) throw new ApplicationException(ReservationServiceProgressCode.SEAT_ID_DUPLICATED);

        var now = clock.instant();
        var seat = Seat.of(id, now);
        var saved = store.save(seat);

        var event = SeatCreatedEvent.of(saved.getId().getRoomId(), saved.getId().getSeatId(), saved.getCreatedAt());
        messagePublisher.publish(event.toMessage(now));

        return SeatCreatedResult.from(saved);
    }
}
