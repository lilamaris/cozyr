package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "seat_occupancy")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatOccupancy {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "room_id", updatable = false, nullable = false))
    private RoomId roomId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "seat_id", updatable = false, nullable = false))
    private SeatId seatId;

    @Column(name = "occupancy_date", nullable = false)
    private LocalDate occupancyDate;

    @Column(name = "schedule_slot_id", nullable = false)
    private UUID scheduleSlotId;

    @Column(name = "released_at")
    private Instant releasedAt;

    private SeatOccupancy(UUID reservationId, RoomId roomId, SeatId seatId, LocalDate occupancyDate, UUID scheduleSlotId, Instant releasedAt) {
        this.reservationId = ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.seatId = ObjectPrecondition.requireNonNull(seatId, "seatId");
        this.occupancyDate = ObjectPrecondition.requireNonNull(occupancyDate, "occupancyDate");
        this.scheduleSlotId = ObjectPrecondition.requireNonNull(scheduleSlotId, "scheduleSlotId");
        this.releasedAt = releasedAt;
    }

    public static SeatOccupancy of(UUID reservationId, RoomId roomId, SeatId seatId, LocalDate occupancyDate, UUID scheduleSlotId) {
        return new SeatOccupancy(reservationId, roomId, seatId, occupancyDate, scheduleSlotId, null);
    }
}
