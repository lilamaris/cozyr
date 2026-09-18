package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomOpPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomOpPolicyJpaAdapter implements RoomPolicyStore {
    private final RoomOpPolicyRepository repository;

    @Override
    public void save(RoomOpPolicy policy) {
        repository.save(policy);
    }

    @Override
    public Optional<RoomOpPolicy> findForUpdate(RoomId roomId) {
        return repository.findForUpdate(roomId);
    }

    @Override
    public RoomUpdateStatus updateDeactivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant deactivatedAt, Instant updatedAt) {
        var updated = repository.updateDeactivatedAtWithAwareByOwner(roomId, userId, deactivatedAt, updatedAt);

        return updated > 0 ? RoomUpdateStatus.UPDATED : RoomUpdateStatus.NOT_OWNED;
    }

    @Override
    public RoomUpdateStatus updateActivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant activatedAt, Instant updatedAt) {
        var updated = repository.updateActivatedAtWithAwareByOwner(roomId, userId, activatedAt, updatedAt);

        return updated > 0 ? RoomUpdateStatus.UPDATED : RoomUpdateStatus.NOT_OWNED;
    }
}
