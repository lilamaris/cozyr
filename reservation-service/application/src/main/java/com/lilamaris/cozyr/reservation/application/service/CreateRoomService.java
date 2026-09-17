package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.internal.RoomPolicyFactory;
import com.lilamaris.cozyr.reservation.application.internal.RoomScheduleSlotFactory;
import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.port.in.CreateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.contract.event.RoomCreatedEvent;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRoomService implements CreateRoomUseCase {
    private final RoomStore store;

    private final RoomScheduleSlotFactory roomScheduleSlotFactory;
    private final RoomScheduleSlotStore roomScheduleSlotStore;

    private final RoomPolicyFactory roomPolicyFactory;
    private final RoomPolicyStore roomPolicyStore;

    private final IdGenerator<UUID> idGenerator;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public RoomCreatedResult create(CreateRoomCommand command) {
        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        var roomId = RoomId.of(idGenerator.generate());
        var room = Room.of(roomId, name, description, now);
        var saved = store.save(room);

        var roomScheduleSlots = roomScheduleSlotFactory.fromProperties(roomId);
        roomScheduleSlotStore.saveAll(roomScheduleSlots);

        var roomOpPolicy = roomPolicyFactory.fromProperties(saved.getId(), saved.getCreatedAt());
        roomPolicyStore.save(roomOpPolicy);

        var event = RoomCreatedEvent.of(roomId.getValue(), saved.getName(), saved.getDescription(), saved.getCreatedAt());
        messagePublisher.publish(event.toMessage(now));

        return RoomCreatedResult.from(saved);
    }
}
