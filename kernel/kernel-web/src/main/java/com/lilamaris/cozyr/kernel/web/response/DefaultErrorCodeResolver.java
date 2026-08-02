package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public class DefaultErrorCodeResolver implements ErrorCodeResolver {
    private final static String SEPARATOR = ".";

    public String resolve(ErrorDescriptor errorDescriptor) {
        ObjectPrecondition.requireNonNull(errorDescriptor, "errorDescriptor");

        var errorReason = errorDescriptor.reason().getCanonicalName();
        var errorType = errorDescriptor.type();

        StringPrecondition.requireNonBlank(errorReason, "errorReason");
        StringPrecondition.requireNonBlank(errorType, "errorType");

        return errorDescriptor.resourceName()
                .map(resourceName -> {
                    StringPrecondition.requireNonBlank(resourceName, "resourceName");
                    return errorReason + SEPARATOR + resourceName + SEPARATOR + errorType;
                })
                .orElseGet(() -> errorReason + SEPARATOR + errorType);
    }
}
