package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @EmbeddedId
    private ReservationId id;

    @Column(name = "reserved_user_id", nullable = false)
    private UUID reservedUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "room_id", updatable = false, nullable = false))
    private RoomId roomId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "seat_id", updatable = false, nullable = false))
    private SeatId seatId;

    @Column(name = "occupancy_date", nullable = false)
    private LocalDate occupancyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Reservation(ReservationId id, UUID reservedUserId, RoomId roomId, SeatId seatId, LocalDate occupancyDate,
                        ReservationStatus status, Instant createdAt, Instant updatedAt) {
        this.id = ObjectPrecondition.requireNonNull(id, "id");
        this.reservedUserId = ObjectPrecondition.requireNonNull(reservedUserId, "reservedUserId");
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.seatId = ObjectPrecondition.requireNonNull(seatId, "seatId");
        this.occupancyDate = ObjectPrecondition.requireNonNull(occupancyDate, "occupancyDate");
        this.status = ObjectPrecondition.requireNonNull(status, "status");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Reservation of(ReservationId id, UUID reservedUserId, RoomId roomId, SeatId seatId, LocalDate occupancyDate, Instant createdAt) {
        return new Reservation(id, reservedUserId, roomId, seatId, occupancyDate, ReservationStatus.RESERVED, createdAt, createdAt);
    }
}
