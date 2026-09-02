package com.lilamaris.cozyr.identity.contract.provider;

import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;

public interface ServiceDescriptor {
    String canonicalName();

    Role defaultRole();

    default Scope defaultScope() {
        return Scope.of(canonicalName(), defaultRole());
    }

    default boolean owns(Scope scope) {
        return canonicalName().equals(scope.service());
    }
}
