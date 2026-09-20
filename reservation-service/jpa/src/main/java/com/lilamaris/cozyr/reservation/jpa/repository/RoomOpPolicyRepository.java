package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RoomOpPolicyRepository extends JpaRepository<RoomOpPolicy, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM RoomOpPolicy p WHERE p.roomId = :roomId")
    Optional<RoomOpPolicy> findForUpdate(@Param("roomId") RoomId roomId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM RoomOpPolicy p WHERE p.roomId = :roomId")
    Optional<RoomOpPolicy> findForShare(@Param("roomId") RoomId roomId);

    @Modifying
    @Query("""
            UPDATE RoomOpPolicy p
            SET p.deactivatedAt = :deactivatedAt,
                p.activatedAt = NULL,
                p.updatedAt = :updatedAt
            WHERE p.roomId = :roomId
                AND EXISTS (
                    SELECT r.id
                    FROM Room r
                    WHERE r.id = p.roomId
                        AND r.userId = :userId
                )
            """)
    int updateDeactivatedAtWithAwareByOwner(
            @Param("roomId") RoomId roomId,
            @Param("userId") UUID userId,
            @Param("deactivatedAt") Instant deactivatedAt,
            @Param("updatedAt") Instant updatedAt
    );

    @Modifying
    @Query("""
            UPDATE RoomOpPolicy p
            SET p.activatedAt = :activatedAt,
                p.deactivatedAt = NULL,
                p.updatedAt = :updatedAt
            WHERE p.roomId = :roomId
                AND EXISTS (
                    SELECT r.id
                    FROM Room r
                    WHERE r.id = p.roomId
                        AND r.userId = :userId
                )
            """)
    int updateActivatedAtWithAwareByOwner(
            @Param("roomId") RoomId roomId,
            @Param("userId") UUID userId,
            @Param("activatedAt") Instant activatedAt,
            @Param("updatedAt") Instant updatedAt
    );
}
