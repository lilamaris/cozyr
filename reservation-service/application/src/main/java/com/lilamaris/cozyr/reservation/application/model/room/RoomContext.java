package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;

public record RoomContext(
        long roomId,
        RoomOpPolicy opPolicy
) {
    public RoomContext {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        ObjectPrecondition.requireNonNull(opPolicy, "opPolicy");
    }

    public static RoomContext of(long roomId, RoomOpPolicy opPolicy) {
        return new RoomContext(roomId, opPolicy);
    }
}
