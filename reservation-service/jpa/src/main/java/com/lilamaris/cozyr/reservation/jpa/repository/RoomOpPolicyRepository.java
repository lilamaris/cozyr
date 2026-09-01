package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomOpPolicyRepository extends JpaRepository<RoomOpPolicy, UUID> {
    Optional<RoomOpPolicy> findByRoomId(Long roomId);
}
