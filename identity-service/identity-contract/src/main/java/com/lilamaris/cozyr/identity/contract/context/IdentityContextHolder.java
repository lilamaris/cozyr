package com.lilamaris.cozyr.identity.contract.context;

import com.lilamaris.cozyr.identity.contract.schema.Identity;

public interface IdentityContextHolder {
    Identity get();

    void set(Identity identity);

    void clear();
}
