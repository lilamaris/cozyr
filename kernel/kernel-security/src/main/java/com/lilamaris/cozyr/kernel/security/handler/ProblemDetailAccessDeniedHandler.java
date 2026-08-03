package com.lilamaris.cozyr.kernel.security.handler;

import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
@NullMarked
public class ProblemDetailAccessDeniedHandler implements AccessDeniedHandler {
    private final ProblemDetailFactory problemDetailFactory;
    private final ServletJsonResponseWriter responseWriter;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        var problem = problemDetailFactory.accessDenied();
        responseWriter.write(response, problem);
    }
}
