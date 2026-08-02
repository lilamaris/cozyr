package com.lilamaris.cozyr.kernel.web.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class ServletJsonResponseWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
        if (response == null) return;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(problemDetail.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}
