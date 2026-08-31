package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "room_op_policy")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomOpPolicy {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "max_reservation_per_user_per_day", nullable = false)
    private int maxReservationPerUserPerDay;

    @Column(name = "max_schedule_per_reservation", nullable = false)
    private int maxSchedulePerReservation;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private RoomOpPolicy(Long roomId, int maxReservationPerUserPerDay, int maxSchedulePerReservation, Instant updatedAt) {
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.maxReservationPerUserPerDay = NumberPrecondition.requirePositive(maxReservationPerUserPerDay, "maxReservationPerUserPerDay");
        this.maxSchedulePerReservation = NumberPrecondition.requirePositive(maxSchedulePerReservation, "maxSchedulePerReservation");
        this.updatedAt = ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static RoomOpPolicy of(Long roomId, int maxReservationPerUserPerDay, int maxSchedulePerReservation, Instant createdAt) {
        return new RoomOpPolicy(roomId, maxReservationPerUserPerDay, maxSchedulePerReservation, createdAt);
    }
}
