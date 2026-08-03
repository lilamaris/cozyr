package com.lilamaris.cozyr.kernel.security.handler;

import com.lilamaris.cozyr.kernel.security.exception.ApplicationAuthenticationException;
import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@RequiredArgsConstructor
@NullMarked
public class ProblemDetailAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ProblemDetailFactory problemDetailFactory;
    private final ServletJsonResponseWriter responseWriter;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        var problem = exception instanceof ApplicationAuthenticationException applicationException
                ? problemDetailFactory.from(applicationException.getApplicationCode())
                : problemDetailFactory.unauthorized();

        responseWriter.write(response, problem);
    }
}
