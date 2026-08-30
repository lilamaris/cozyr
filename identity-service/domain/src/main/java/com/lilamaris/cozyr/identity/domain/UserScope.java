package com.lilamaris.cozyr.identity.domain;

import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_scope")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserScope {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "service", nullable = false)
    private String service;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private UserScope(UUID userId, String service, Role role, Instant createdAt) {
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.service = StringPrecondition.requireNonBlank(service, "service");
        this.role = ObjectPrecondition.requireNonNull(role, "role");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static UserScope of(UUID userId, String service, Role role, Instant createdAt) {
        return new UserScope(userId, service, role, createdAt);
    }

    public static UserScope from(UUID userId, Scope scope, Instant createdAt) {
        ObjectPrecondition.requireNonNull(scope, "scope");
        return new UserScope(userId, scope.service(), scope.role(), createdAt);
    }

    public Scope toScope() {
        return Scope.of(service, role);
    }
}
