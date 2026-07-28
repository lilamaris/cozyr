package com.lilamaris.cozyr.identity.domain;

import com.lilamaris.cozyr.identity.contract.schema.Identity;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cozyr_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "version", nullable = false)
    private long version;

    private User(String displayName, Instant createdAt, Instant updatedAt, long version) {
        this.displayName = StringPrecondition.requireNonBlank(displayName, "displayName");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        this.version = NumberPrecondition.requireNonNegative(version, "version");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static User of(String displayName, Instant createdAt) {
        return new User(displayName, createdAt, null, 0L);
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public long version() {
        return version;
    }

    public void updateDisplayName(String displayName, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.displayName = StringPrecondition.requireNonBlank(displayName, "displayName");
        this.version += 1;
    }
}
