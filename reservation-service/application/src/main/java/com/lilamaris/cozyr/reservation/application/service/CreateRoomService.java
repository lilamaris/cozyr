package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.internal.room.RoomInternalCreateService;
import com.lilamaris.cozyr.reservation.application.port.in.CreateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.contract.event.RoomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateRoomService implements CreateRoomUseCase {
    private final RoomInternalCreateService roomInternalCreateService;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public RoomCreatedResult create(CreateRoomCommand command) {
        var now = clock.instant();
        var createdRoom = roomInternalCreateService.create(
                command.name(),
                command.description(),
                command.userId(),
                now
        );

        var event = RoomCreatedEvent.of(
                createdRoom.roomId(),
                createdRoom.name(),
                createdRoom.description(),
                createdRoom.createdAt()
        );
        messagePublisher.publish(event.toMessage(now));

        return createdRoom;
    }
}
