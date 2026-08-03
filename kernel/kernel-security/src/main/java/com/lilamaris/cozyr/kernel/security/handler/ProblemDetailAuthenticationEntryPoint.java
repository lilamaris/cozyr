package com.lilamaris.cozyr.kernel.security.handler;

import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
@NullMarked
public class ProblemDetailAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ProblemDetailFactory problemDetailFactory;
    private final ServletJsonResponseWriter responseWriter;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        var problem = problemDetailFactory.unauthorized();
        responseWriter.write(response, problem);
    }
}
