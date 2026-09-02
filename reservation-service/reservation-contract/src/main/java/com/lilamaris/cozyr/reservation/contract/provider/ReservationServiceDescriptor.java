package com.lilamaris.cozyr.reservation.contract.provider;

import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.schema.Role;

public class ReservationServiceDescriptor implements ServiceDescriptor {
    public static final String CANONICAL_NAME = "reservation";

    @Override
    public String canonicalName() {
        return CANONICAL_NAME;
    }

    @Override
    public Role defaultRole() {
        return Role.USER;
    }
}
