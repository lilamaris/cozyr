package com.lilamaris.cozyr.board.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSnapshot {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "last_updated_at", nullable = false)
    private Instant lastUpdatedAt;

    public UserSnapshot(UUID userId, String displayName, Instant lastUpdatedAt) {
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.displayName = StringPrecondition.requireNonBlank(displayName, "displayName");
        this.lastUpdatedAt = ObjectPrecondition.requireNonNull(lastUpdatedAt, "lastUpdatedAt");
    }

    public static UserSnapshot of(UUID userId, String displayName, Instant lastUpdatedAt) {
        return new UserSnapshot(userId, displayName, lastUpdatedAt);
    }

    public void updateDisplayName(String displayName, Instant updatedAt) {
        this.lastUpdatedAt = ObjectPrecondition.requireNonNull(updatedAt, "lastUpdatedAt");
        this.displayName = StringPrecondition.requireNonBlank(displayName, "displayName");
    }
}
