package com.lilamaris.cozyr.identity.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HttpStatusResolver {
    public HttpStatus resolve(String type) {
        return switch (type) {
            case "not-found" -> HttpStatus.NOT_FOUND;
            case "duplicated" -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
