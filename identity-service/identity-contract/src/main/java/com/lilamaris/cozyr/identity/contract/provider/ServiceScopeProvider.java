package com.lilamaris.cozyr.identity.contract.provider;

import com.lilamaris.cozyr.identity.contract.schema.Scope;

public interface ServiceScopeProvider {
    Scope provide();

    default boolean isScopeMatched(Scope other) {
        return provide().service().equals(other.service());
    }
}
