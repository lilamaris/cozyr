package com.lilamaris.cozyr.identity.application.model.user;

import org.jspecify.annotations.Nullable;

public record UserFilter(
        @Nullable String displayName
) {
    public static UserFilter empty() {
        return new UserFilter(null);
    }

    public UserFilter withDisplayName(String displayName) {
        return new UserFilter(displayName);
    }
}
