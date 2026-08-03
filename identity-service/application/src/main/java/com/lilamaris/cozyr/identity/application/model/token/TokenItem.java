package com.lilamaris.cozyr.identity.application.model.token;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record TokenItem(
        String value,
        long expiresIn
) {
    public TokenItem {
        StringPrecondition.requireNonBlank(value, "value");
        NumberPrecondition.requirePositive(expiresIn, "expiresIn");
    }

    public static TokenItem of(String value, long expiresIn) {
        return new TokenItem(value, expiresIn);
    }
}
