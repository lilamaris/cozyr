package com.lilamaris.cozyr.identity.contract.schema;

import java.util.UUID;

public interface Identity {
    UUID id();

    String displayName();
}
