package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomFilter;
import jakarta.annotation.Nullable;

public record ListRoomSummaryQuery(
        RoomFilter filter,
        @Nullable RoomCursor cursor,
        int size
) {
    public ListRoomSummaryQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requireNonNegative(size, "size");
    }

    public static ListRoomSummaryQuery of(RoomFilter filter, RoomCursor cursor, int size) {
        return new ListRoomSummaryQuery(filter, cursor, size);
    }
}
