package com.lilamaris.cozyr.identity.contract.provider;

import com.lilamaris.cozyr.identity.contract.schema.Scope;

public interface ServiceScopeProvider {
    Scope provide();
}
