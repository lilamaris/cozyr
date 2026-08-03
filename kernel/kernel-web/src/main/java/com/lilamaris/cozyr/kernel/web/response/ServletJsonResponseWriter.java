package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class ServletJsonResponseWriter implements ServletResponseWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
        write(response, HttpStatus.valueOf(problemDetail.getStatus()), problemDetail);
    }

    @Override
    public void write(HttpServletResponse response, HttpStatus status, Object body) throws IOException {
        ObjectPrecondition.requireNonNull(response, "response");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    @Override
    public void noContent(HttpServletResponse response) {
        ObjectPrecondition.requireNonNull(response, "response");

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
