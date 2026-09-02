package com.lilamaris.cozyr.identity.application.config;

import com.lilamaris.cozyr.identity.application.port.out.JwksReader;
import com.lilamaris.cozyr.identity.contract.codec.ScopeCodec;
import com.lilamaris.cozyr.identity.contract.provider.IdentityServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.random.RandomGenerator;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.identity-service.application",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    Clock clock(ApplicationProperties properties) {
        return Clock.system(properties.timezone());
    }

    @Bean
    @ConditionalOnMissingBean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    RandomGenerator randomGenerator() {
        return new SecureRandom();
    }

    @Bean
    @ConditionalOnMissingBean
    JwtEncoder jwtEncoder(JwksReader reader) {
        var keyPair = reader.findSignableKey();
        var rsaKey = new RSAKey.Builder(keyPair.publicKey())
                .privateKey(keyPair.privateKey())
                .keyID(keyPair.kid())
                .build();
        JWKSource<SecurityContext> source = (selector, context) -> selector
                .select(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(source);
    }

    @Bean
    @ConditionalOnMissingBean
    ScopeCodec scopeCodec() {
        return new ScopeCodec();
    }

    @Bean
    ServiceDescriptor serviceDescriptor() {
        return new IdentityServiceDescriptor();
    }
}
