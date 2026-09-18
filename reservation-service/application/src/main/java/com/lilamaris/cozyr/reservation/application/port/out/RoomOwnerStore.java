package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateParams;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.time.Instant;
import java.util.UUID;

public interface RoomOwnerStore {
    RoomUpdateStatus updateByOwned(RoomId roomId, UUID userId, RoomUpdateParams params, Instant updatedAt);
}
