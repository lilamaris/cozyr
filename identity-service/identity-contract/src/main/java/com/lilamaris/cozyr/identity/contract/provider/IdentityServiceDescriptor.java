package com.lilamaris.cozyr.identity.contract.provider;

import com.lilamaris.cozyr.identity.contract.schema.Role;

public class IdentityServiceDescriptor implements ServiceDescriptor {
    public static final String CANONICAL_NAME = "identity";

    @Override
    public String canonicalName() {
        return CANONICAL_NAME;
    }

    @Override
    public Role defaultRole() {
        return Role.USER;
    }
}
