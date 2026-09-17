package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;

import java.util.UUID;

public record RoomContext(
        UUID roomId,
        RoomOpPolicy opPolicy
) {
    public RoomContext {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(opPolicy, "opPolicy");
    }

    public static RoomContext of(UUID roomId, RoomOpPolicy opPolicy) {
        return new RoomContext(roomId, opPolicy);
    }
}
