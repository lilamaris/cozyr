package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.*;
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "room_id", updatable = false, nullable = false))
    private RoomId roomId;

    @Column(name = "max_reservation_per_user_per_day", nullable = false)
    private int maxReservationPerUserPerDay;

    @Column(name = "max_schedule_per_reservation", nullable = false)
    private int maxSchedulePerReservation;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "deactivated_at")
    private Instant deactivatedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private RoomOpPolicy(RoomId roomId, int maxReservationPerUserPerDay, int maxSchedulePerReservation, Instant activatedAt, Instant deactivatedAt, Instant createdAt, Instant updatedAt) {
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.maxReservationPerUserPerDay = NumberPrecondition.requirePositive(maxReservationPerUserPerDay, "maxReservationPerUserPerDay");
        this.maxSchedulePerReservation = NumberPrecondition.requirePositive(maxSchedulePerReservation, "maxSchedulePerReservation");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }

        if ((activatedAt != null) == (deactivatedAt != null))
            throw new IllegalArgumentException();

        if (activatedAt != null) {
            this.activatedAt = TimePrecondition.requireAfterOrEqual(activatedAt, createdAt, "activatedAt", "createdAt");
        }

        if (deactivatedAt != null) {
            this.deactivatedAt = TimePrecondition.requireAfterOrEqual(deactivatedAt, createdAt, "deactivatedAt", "createdAt");
        }
    }

    public static RoomOpPolicy of(RoomId roomId, int maxReservationPerUserPerDay, int maxSchedulePerReservation, Instant createdAt) {
        return new RoomOpPolicy(roomId, maxReservationPerUserPerDay, maxSchedulePerReservation, createdAt, null, createdAt, createdAt);
    }

    public boolean allowsScheduleCount(int scheduleCount) {
        NumberPrecondition.requirePositive(scheduleCount, "scheduleCount");
        return scheduleCount <= maxSchedulePerReservation;
    }

    public boolean allowsReservation(Instant startAt, Instant endsAt) {
        return activatedAt != null
                ? !startAt.isBefore(activatedAt)
                : !endsAt.isAfter(deactivatedAt);
    }

    public boolean isActiveAt(Instant now) {
        return activatedAt != null
                ? !now.isBefore(activatedAt)
                : now.isBefore(deactivatedAt);
    }
}
