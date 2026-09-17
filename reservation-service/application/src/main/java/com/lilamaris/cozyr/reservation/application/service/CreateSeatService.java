package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.port.in.CreateSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.SeatCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatStore;
import com.lilamaris.cozyr.reservation.contract.event.SeatCreatedEvent;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSeatService implements CreateSeatUseCase {
    private final SeatReader reader;
    private final SeatStore store;

    private final RoomReader roomReader;
    private final MessagePublisher messagePublisher;
    private final IdGenerator<UUID> idGenerator;
    private final Clock clock;

    @Override
    @Transactional
    public SeatCreatedResult create(CreateSeatCommand command) {
        var roomId = command.roomId();

        var roomExists = roomReader.existsById(roomId);
        if (!roomExists) throw new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND);

        var now = clock.instant();
        var seatId = SeatId.of(idGenerator.generate());
        var code = command.code();
        var seat = Seat.of(seatId, roomId, code, now);
        var saved = store.save(seat);

        var event = SeatCreatedEvent.of(roomId.getValue(), seatId.getValue(), now);
        messagePublisher.publish(event.toMessage(now));

        return SeatCreatedResult.from(saved);
    }
}
