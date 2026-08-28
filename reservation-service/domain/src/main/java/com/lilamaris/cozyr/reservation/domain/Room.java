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
@Table(name = "room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Room(String name, String description, Instant createdAt, Instant updatedAt) {
        this.name = StringPrecondition.requireNonBlank(name, "name");
        this.description = StringPrecondition.requireNonBlank(description, "description");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Room of(String name, String description, Instant createdAt) {
        return new Room(name, description, createdAt, createdAt);
    }

    public void updateName(String name, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.name = StringPrecondition.requireNonBlank(name, "name");
    }

    public void updateDescription(String description, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.description = StringPrecondition.requireNonBlank(description, "description");
    }
}
