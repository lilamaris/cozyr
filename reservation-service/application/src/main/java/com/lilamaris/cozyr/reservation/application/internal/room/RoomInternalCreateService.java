package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomInternalCreateService {
    private final RoomStore roomStore;
    private final RoomScheduleSlotFactory roomScheduleSlotFactory;
    private final RoomScheduleSlotStore roomScheduleSlotStore;
    private final RoomPolicyFactory roomPolicyFactory;
    private final RoomPolicyStore roomPolicyStore;

    private final IdGenerator<UUID> idGenerator;

    public RoomCreatedResult create(String name, String description, UUID userId, Instant createdAt) {
        var roomId = RoomId.of(idGenerator.generate());
        var room = Room.of(roomId, name, description, userId, createdAt);
        var roomScheduleSlots = roomScheduleSlotFactory.fromProperties(roomId);
        var roomOpPolicy = roomPolicyFactory.fromProperties(roomId, createdAt);

        roomStore.save(room);
        roomScheduleSlotStore.saveAll(roomScheduleSlots);
        roomPolicyStore.save(roomOpPolicy);

        return RoomCreatedResult.from(room);
    }
}
