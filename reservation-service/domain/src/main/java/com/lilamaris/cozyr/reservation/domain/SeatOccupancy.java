package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private SeatId seatId;

    @Column(name = "occupancy_date", nullable = false)
    private LocalDate occupancyDate;

    @Column(name = "schedule_slot_id", nullable = false)
    private UUID scheduleSlotId;

    private SeatOccupancy(UUID reservationId, SeatId seatId, LocalDate occupancyDate, UUID scheduleSlotId) {
        this.reservationId = ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        this.seatId = ObjectPrecondition.requireNonNull(seatId, "seatId");
        this.occupancyDate = ObjectPrecondition.requireNonNull(occupancyDate, "occupancyDate");
        this.scheduleSlotId = ObjectPrecondition.requireNonNull(scheduleSlotId, "scheduleSlotId");
    }

    public static SeatOccupancy of(UUID reservationId, SeatId seatId, LocalDate occupancyDate, UUID scheduleSlotId) {
        return new SeatOccupancy(reservationId, seatId, occupancyDate, scheduleSlotId);
    }
}
