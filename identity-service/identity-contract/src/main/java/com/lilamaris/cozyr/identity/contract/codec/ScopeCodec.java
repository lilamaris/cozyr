package com.lilamaris.cozyr.identity.contract.codec;

import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.Set;
import java.util.stream.Collectors;

public class ScopeCodec {
    public static final String SEPARATOR = ".";

    public String encode(Scope scope) {
        ObjectPrecondition.requireNonNull(scope, "scope");

        var service = StringPrecondition.requireNotContain(scope.service(), SEPARATOR, "scope.service");
        var role = StringPrecondition.requireNotContain(scope.role().getCanonicalName(), SEPARATOR, "scope.role");

        return service + SEPARATOR + role;
    }

    public Set<String> encode(Set<Scope> scopes) {
        return scopes.stream().map(this::encode).collect(Collectors.toUnmodifiableSet());
    }

    public Scope decode(String encoded) {
        var value = StringPrecondition.requireNonBlank(encoded, "encoded");

        var parts = value.split(",");
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank())
            throw new IllegalArgumentException("Scope must have format '<service>.<role>': " + encoded);
        var service = parts[0];
        var role = Role.from(parts[1]);

        return Scope.of(service, role);
    }

    public Set<Scope> decode(Set<String> encoded) {
        return encoded.stream().map(this::decode).collect(Collectors.toUnmodifiableSet());
    }
}
