package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.contract.schema.Scope;

import java.util.List;

public interface ServiceScopeReader {
    List<Scope> getAllScopes();
}
