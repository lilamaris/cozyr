package com.lilamaris.cozyr.board.security.config;

import com.lilamaris.cozyr.identity.resource.server.filter.IdentityAuthenticationConverter;
import com.lilamaris.cozyr.identity.resource.server.filter.IdentityContextBindingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtDecoder jwtDecoder,
            IdentityContextBindingFilter filter,
            IdentityAuthenticationConverter converter
    ) {
        http
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwt -> jwt
                                        .decoder(jwtDecoder)
                                        .jwtAuthenticationConverter(converter)
                        )
                )
                .addFilterAfter(filter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
