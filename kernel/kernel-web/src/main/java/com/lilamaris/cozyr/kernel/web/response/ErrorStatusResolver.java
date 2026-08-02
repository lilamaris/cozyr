package com.lilamaris.cozyr.kernel.web.response;

import org.springframework.http.HttpStatus;

public interface ErrorStatusResolver {
    HttpStatus resolve(String errorType);
}
