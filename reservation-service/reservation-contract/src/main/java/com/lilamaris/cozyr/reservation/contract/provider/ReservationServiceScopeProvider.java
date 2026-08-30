package com.lilamaris.cozyr.reservation.contract.provider;

import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeProvider;
import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;

public class ReservationServiceScopeProvider implements ServiceScopeProvider {
    @Override
    public Scope provide() {
        return Scope.of("reservation", Role.USER);
    }
}
