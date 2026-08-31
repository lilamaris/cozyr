package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomOpPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomOpPolicyJpaAdapter implements RoomPolicyStore {
    private final RoomOpPolicyRepository repository;

    @Override
    public RoomOpPolicy saveOp(RoomOpPolicy policy) {
        return repository.save(policy);
    }
}
