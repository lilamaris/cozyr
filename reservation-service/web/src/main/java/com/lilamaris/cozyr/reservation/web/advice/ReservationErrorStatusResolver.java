package com.lilamaris.cozyr.reservation.web.advice;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.web.response.ErrorStatusResolver;
import org.springframework.http.HttpStatus;

public class ReservationErrorStatusResolver implements ErrorStatusResolver {
    @Override
    public HttpStatus resolve(String errorType) {
        StringPrecondition.requireNonBlank(errorType, "errorType");
        return switch (errorType) {
            case "bad-request", "bad_request" -> HttpStatus.BAD_REQUEST;
            case "not-found" -> HttpStatus.NOT_FOUND;
            case "duplicated", "daily-reservation-limit-exceeded" -> HttpStatus.CONFLICT;
            case "schedule-count-limit-exceeded" -> HttpStatus.UNPROCESSABLE_CONTENT;
            case "access-denied", "forbidden" -> HttpStatus.FORBIDDEN;
            case "unauthorized" -> HttpStatus.UNAUTHORIZED;
            case "internal-server-error", "internal_server_error", "unexpected-error", "unexpected_error" ->
                    HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
