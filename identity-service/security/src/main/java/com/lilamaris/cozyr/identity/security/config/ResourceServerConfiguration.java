package com.lilamaris.cozyr.identity.security.config;

import com.lilamaris.cozyr.identity.application.port.out.JwksReader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class ResourceServerConfiguration {
    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder(JwksReader reader) {
        var keys = reader.findVerifiableKeys().stream()
                .<JWK>map(key ->
                        new RSAKey.Builder(key.publicKey())
                                .keyID(key.kid())
                                .keyUse(KeyUse.SIGNATURE)
                                .algorithm(JWSAlgorithm.RS256)
                                .build()
                )
                .toList();

        JWKSource<SecurityContext> source = (selector, context) -> selector.select(new JWKSet(keys));

        return NimbusJwtDecoder.withJwkSource(source).build();
    }
}
