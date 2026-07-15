package com.lilamaris.cozyr.identity.contract.schema;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.Locale;

public record Scope(
        String service,
        Role role
) {
    public Scope {
        service = StringPrecondition
                .requireNonBlank(service, "service")
                .trim()
                .toLowerCase(Locale.ROOT);
        ObjectPrecondition.requireNonNull(role, "role");
    }

    public static Scope of(String service, Role role) {
        return new Scope(service, role);
    }
}
