package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.springframework.http.HttpStatus;

public class DefaultErrorStatusResolver implements ErrorStatusResolver {
    public HttpStatus resolve(String errorType) {
        StringPrecondition.requireNonBlank(errorType, "errorType");
        return switch (errorType) {
            case "bad-request", "bad_request" -> HttpStatus.BAD_REQUEST;
            case "not-found" -> HttpStatus.NOT_FOUND;
            case "duplicated" -> HttpStatus.CONFLICT;
            case "access-denied", "forbidden" -> HttpStatus.FORBIDDEN;
            case "internal-server-error", "internal_server_error", "unexpected-error", "unexpected_error" ->
                    HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
