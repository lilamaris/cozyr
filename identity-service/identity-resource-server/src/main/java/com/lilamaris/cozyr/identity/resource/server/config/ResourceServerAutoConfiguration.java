package com.lilamaris.cozyr.identity.resource.server.config;

import com.lilamaris.cozyr.identity.contract.codec.ScopeCodec;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.contract.context.ThreadLocalIdentityContextHolder;
import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.lilamaris.cozyr.identity.resource.server.filter.IdentityAuthenticationConverter;
import com.lilamaris.cozyr.identity.resource.server.filter.IdentityContextBindingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.identity-service.resource-server",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({ResourceServerProperties.class})
public class ResourceServerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder(ResourceServerProperties properties) {
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
    ScopeCodec scopeCodec() {
        return new ScopeCodec();
    }

    @Bean
    @ConditionalOnMissingBean
    IdentityAuthenticationConverter identityAuthenticationConverter(ScopeCodec scopeCodec, ServiceDescriptor serviceDescriptor) {
        return new IdentityAuthenticationConverter(scopeCodec, serviceDescriptor);
    }
}
