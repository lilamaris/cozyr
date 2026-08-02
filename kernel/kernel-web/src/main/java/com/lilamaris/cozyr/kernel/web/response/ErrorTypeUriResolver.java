package com.lilamaris.cozyr.kernel.web.response;

import java.net.URI;

public interface ErrorTypeUriResolver {
    URI resolve(String errorType);
}
