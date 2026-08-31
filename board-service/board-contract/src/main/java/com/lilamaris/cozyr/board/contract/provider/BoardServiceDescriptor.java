package com.lilamaris.cozyr.board.contract.provider;

import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.schema.Role;

public class BoardServiceDescriptor implements ServiceDescriptor {
    public static final String CANONICAL_NAME = "board";

    @Override
    public String canonicalName() {
        return CANONICAL_NAME;
    }

    @Override
    public Role defaultRole() {
        return Role.USER;
    }
}
