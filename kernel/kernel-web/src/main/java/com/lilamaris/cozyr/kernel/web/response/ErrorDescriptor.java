package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;

import java.util.Optional;

public interface ErrorDescriptor {
    ProcessReason reason();

    default Optional<String> resourceName() {
        return Optional.empty();
    }

    String type();

    String message();
}
