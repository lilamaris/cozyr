package com.lilamaris.cozyr.kernel.web.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface ServletResponseWriter {
    void write(HttpServletResponse response, HttpStatus status, Object body) throws IOException;
}
