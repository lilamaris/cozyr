package com.lilamaris.cozyr.kernel.web.response;

public interface ErrorCodeResolver {
    String resolve(ErrorDescriptor errorDescriptor);
}
