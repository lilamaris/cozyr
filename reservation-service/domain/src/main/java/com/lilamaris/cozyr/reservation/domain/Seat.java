package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Seat(SeatId id, Instant createdAt, Instant updatedAt) {
        this.id = ObjectPrecondition.requireNonNull(id, "id");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Seat of(SeatId id, Instant createdAt) {
        return new Seat(id, createdAt, createdAt);
    }
}
