package com.lilamaris.cozyr.identity.security.config;

import com.lilamaris.cozyr.identity.application.port.in.AuthenticateCredentialUseCase;
import com.lilamaris.cozyr.identity.application.port.in.IssueTokenUseCase;
import com.lilamaris.cozyr.identity.application.port.in.RegisterCredentialUseCase;
import com.lilamaris.cozyr.identity.security.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.cozyr.identity.security.credential.filter.JsonCredentialSignUpProcessingFilter;
import com.lilamaris.cozyr.identity.security.credential.handler.CredentialAuthenticationSuccessHandler;
import com.lilamaris.cozyr.identity.security.credential.provider.CredentialSignInProvider;
import com.lilamaris.cozyr.identity.security.credential.provider.CredentialSignUpProvider;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAuthenticationFailureHandler;
import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import com.lilamaris.cozyr.kernel.web.response.ServletResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class CredentialSecurityConfiguration {
    @Bean
    CredentialAuthenticationSuccessHandler credentialAuthenticationSuccessHandler(IssueTokenUseCase useCase, ServletResponseWriter servletResponseWriter) {
        return new CredentialAuthenticationSuccessHandler(useCase, servletResponseWriter);
    }

    @Bean
    ProblemDetailAuthenticationFailureHandler credentialAuthenticationFailureHandler(
            ProblemDetailFactory problemDetailFactory,
            ServletJsonResponseWriter responseWriter
    ) {
        return new ProblemDetailAuthenticationFailureHandler(problemDetailFactory, responseWriter);
    }

    @Bean
    JsonCredentialSignInProcessingFilter jsonCredentialSignInProcessingFilter(
            AuthenticationManager authenticationManager,
            CredentialAuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper
    ) {
        var matcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/sign-in");
        var filter = new JsonCredentialSignInProcessingFilter(matcher, objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

    @Bean
    JsonCredentialSignUpProcessingFilter jsonCredentialSignUpProcessingFilter(
            AuthenticationManager authenticationManager,
            CredentialAuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper
    ) {
        var matcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/sign-up");
        var filter = new JsonCredentialSignUpProcessingFilter(matcher, objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

    @Bean
    CredentialSignInProvider credentialSignInProvider(AuthenticateCredentialUseCase useCase) {
        return new CredentialSignInProvider(useCase);
    }

    @Bean
    CredentialSignUpProvider credentialSignUpProvider(RegisterCredentialUseCase useCase) {
        return new CredentialSignUpProvider(useCase);
    }
}
