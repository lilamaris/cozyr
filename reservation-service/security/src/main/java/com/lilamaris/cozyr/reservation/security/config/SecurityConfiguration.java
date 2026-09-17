package com.lilamaris.cozyr.reservation.security.config;

import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAccessDeniedHandler;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAuthenticationEntryPoint;
import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    ProblemDetailAccessDeniedHandler problemDetailAccessDeniedHandler(
            ProblemDetailFactory problemDetailFactory,
            ServletJsonResponseWriter responseWriter
    ) {
        return new ProblemDetailAccessDeniedHandler(problemDetailFactory, responseWriter);
    }

    @Bean
    ProblemDetailAuthenticationEntryPoint problemDetailAuthenticationEntryPoint(
            ProblemDetailFactory problemDetailFactory,
            ServletJsonResponseWriter responseWriter
    ) {
        return new ProblemDetailAuthenticationEntryPoint(problemDetailFactory, responseWriter);
    }

    @Bean
    ServletJsonResponseWriter servletJsonResponseWriter(ObjectMapper objectMapper) {
        return new ServletJsonResponseWriter(objectMapper);
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/error",
                                "/actuator/prometheus",
                                "/actuator/health",
                                "/actuator/health/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}
