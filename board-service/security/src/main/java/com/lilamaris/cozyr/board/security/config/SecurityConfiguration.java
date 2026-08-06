package com.lilamaris.cozyr.board.security.config;

import com.lilamaris.cozyr.identity.resource.server.filter.IdentityAuthenticationConverter;
import com.lilamaris.cozyr.identity.resource.server.filter.IdentityContextBindingFilter;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAccessDeniedHandler;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAuthenticationEntryPoint;
import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer> jwtConfigurerCustomizer(
            JwtDecoder jwtDecoder,
            IdentityAuthenticationConverter authenticationConverter
    ) {
        return jwt -> jwt
                .decoder(jwtDecoder)
                .jwtAuthenticationConverter(authenticationConverter);
    }

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
            Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer> jwtConfigurerCustomizer,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler,
            IdentityContextBindingFilter filter
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
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(jwtConfigurerCustomizer)
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                .addFilterAfter(filter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
