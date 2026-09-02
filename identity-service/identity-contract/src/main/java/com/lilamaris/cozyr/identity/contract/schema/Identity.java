package com.lilamaris.cozyr.identity.contract.schema;

import java.util.Set;
import java.util.UUID;

public interface Identity {
    UUID id();

    String displayName();

    Set<Scope> scopes();

    long version();
}
