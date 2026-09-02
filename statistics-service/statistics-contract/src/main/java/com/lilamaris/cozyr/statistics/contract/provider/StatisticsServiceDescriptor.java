package com.lilamaris.cozyr.statistics.contract.provider;

import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.schema.Role;

public class StatisticsServiceDescriptor implements ServiceDescriptor {
    public static final String CANONICAL_NAME = "statistics";

    @Override
    public String canonicalName() {
        return CANONICAL_NAME;
    }

    @Override
    public Role defaultRole() {
        return Role.USER;
    }
}
