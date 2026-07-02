package com.lilamaris.cozyr.kernel.core.condition;

public class StringPrecondition {
    public static String requireNonBlank(String value, String name) {
        ObjectPrecondition.requireNonNull(value, name);
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank.");
        return value;
    }
}
