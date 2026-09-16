package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    @EmbeddedId
    private SeatId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "room_id", updatable = false, nullable = false))
    private RoomId roomId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Seat(SeatId id, RoomId roomId, String code, Instant createdAt, Instant updatedAt) {
        this.id = ObjectPrecondition.requireNonNull(id, "id");
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.code = StringPrecondition.requireNonBlank(code, "code");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Seat of(SeatId id, RoomId roomId, String code, Instant createdAt) {
        return new Seat(id, roomId, code, createdAt, createdAt);
    }
}
