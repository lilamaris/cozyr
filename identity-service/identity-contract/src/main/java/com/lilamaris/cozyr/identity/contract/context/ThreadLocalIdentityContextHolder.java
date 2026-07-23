package com.lilamaris.cozyr.identity.contract.context;

import com.lilamaris.cozyr.identity.contract.schema.Identity;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

public class ThreadLocalIdentityContextHolder implements IdentityContextHolder {
    private static final ThreadLocal<Identity> T = new ThreadLocal<>();

    @Override
    public Identity get() {
        return T.get();
    }

    @Override
    public void set(Identity identity) {
        ObjectPrecondition.requireNonNull(identity, "identity");
        T.set(identity);
    }

    @Override
    public void clear() {
        T.remove();
    }
}
