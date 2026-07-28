package com.lilamaris.cozyr.board.application.model.user;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UserProjection(
        UUID userId,
        String displayName
) {
    public UserProjection {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
    }

    public static UserProjection of(UUID userId, String displayName) {
        return new UserProjection(userId, displayName);
    }
}
