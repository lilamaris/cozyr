package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class DefaultErrorTypeUriResolver implements ErrorTypeUriResolver {
    private final static String ERROR_PATH = "errors/";
    private final URI baseUrl;

    public URI resolve(String errorType) {
        StringPrecondition.requireNonBlank(errorType, "errorType");
        return baseUrl
                .resolve(ERROR_PATH)
                .resolve(errorType);
    }
}
