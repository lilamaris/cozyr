package com.lilamaris.cozyr.board.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HttpStatusResolver {
    public HttpStatus resolve(String type) {
        return switch (type) {
            case "not-found" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
