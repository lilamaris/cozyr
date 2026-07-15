package com.lilamaris.cozyr.identity.contract.schema;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("guest"),
    USER("user"),
    MODERATOR("mod"),
    ADMINISTRATOR("admin");

    private static final Map<String, Role> CANONICAL_NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(
                    Role::getCanonicalName,
                    Function.identity()
            ));

    private final String canonicalName;

    public static Role from(String value) {
        StringPrecondition.requireNonBlank(value, "value");

        var normalized = value.trim().toLowerCase(Locale.ROOT);
        var role = CANONICAL_NAME_MAP.get(normalized);

        if (role == null)
            throw new IllegalArgumentException("Unknown role: " + value);

        return role;
    }

    public boolean isGuest() {
        return this == GUEST;
    }

    public boolean isUser() {
        return this == USER;
    }

    public boolean isModerator() {
        return this == MODERATOR;
    }

    public boolean isAdministrator() {
        return this == ADMINISTRATOR;
    }
}
