package com.lilamaris.cozyr.board.security.config;

import com.lilamaris.cozyr.board.security.filter.IdentityAuthenticationConverter;
import com.lilamaris.cozyr.board.security.filter.IdentityContextBindingFilter;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.contract.context.ThreadLocalIdentityContextHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.board-service.security",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder(SecurityProperties properties) {
        return NimbusJwtDecoder.withJwkSetUri(properties.jwksUri().toString()).build();
    }

    @Bean
    @ConditionalOnMissingBean
    IdentityContextHolder identityContextHolder() {
        return new ThreadLocalIdentityContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    IdentityContextBindingFilter identityContextBindingFilter(IdentityContextHolder holder) {
        return new IdentityContextBindingFilter(holder);
    }

    @Bean
    @ConditionalOnMissingBean
    IdentityAuthenticationConverter identityAuthenticationConverter() {
        return new IdentityAuthenticationConverter();
    }

    @Bean
    @ConditionalOnBean({JwtDecoder.class, IdentityContextBindingFilter.class})
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
