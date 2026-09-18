package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RoomPolicyStore {
    void save(RoomOpPolicy policy);

    Optional<RoomOpPolicy> findForUpdate(RoomId roomId);

    RoomUpdateStatus updateDeactivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant deactivatedAt, Instant updatedAt);

    RoomUpdateStatus updateActivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant activatedAt, Instant updatedAt);
}
