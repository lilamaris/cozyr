package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.internal.RoomPolicyFactory;
import com.lilamaris.cozyr.reservation.application.internal.RoomScheduleSlotFactory;
import com.lilamaris.cozyr.reservation.application.port.in.CreateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateRoomService implements CreateRoomUseCase {
    private final RoomStore store;

    private final RoomScheduleSlotFactory roomScheduleSlotFactory;
    private final RoomScheduleSlotStore roomScheduleSlotStore;

    private final RoomPolicyFactory roomPolicyFactory;
    private final RoomPolicyStore roomPolicyStore;

    private final Clock clock;

    @Override
    @Transactional
    public RoomCreatedResult create(CreateRoomCommand command) {
        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        var room = Room.of(name, description, now);
        var saved = store.save(room);

        var roomScheduleSlots = roomScheduleSlotFactory.fromProperties(saved.getId());
        roomScheduleSlotStore.saveAll(roomScheduleSlots);

        var roomOpPolicy = roomPolicyFactory.fromProperties(saved.getId(), saved.getCreatedAt());
        roomPolicyStore.saveOp(roomOpPolicy);

        return RoomCreatedResult.from(room);
    }
}
