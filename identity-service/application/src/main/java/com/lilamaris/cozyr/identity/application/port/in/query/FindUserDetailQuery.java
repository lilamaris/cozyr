package com.lilamaris.cozyr.identity.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record FindUserDetailQuery(UUID userId) {
    public FindUserDetailQuery {
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static FindUserDetailQuery of(UUID userId) {
        return new FindUserDetailQuery(userId);
    }
}
