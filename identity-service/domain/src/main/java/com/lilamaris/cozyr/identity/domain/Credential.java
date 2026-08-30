package com.lilamaris.cozyr.identity.domain;

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
@Table(name = "credential")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Credential(UUID userId, String email, String passwordHash, Instant createdAt, Instant updatedAt) {
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.email = StringPrecondition.requireNonBlank(email, "email");
        this.passwordHash = StringPrecondition.requireNonBlank(passwordHash, "passwordHash");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Credential of(UUID userId, String email, String passwordHash, Instant createdAt) {
        return new Credential(userId, email, passwordHash, createdAt, null);
    }

    public void updatePasswordHash(String passwordHash, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.passwordHash = StringPrecondition.requireNonBlank(passwordHash, "passwordHash");
    }
}
