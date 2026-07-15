package com.lilamaris.cozyr.identity.io.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.jwks.io")
public record FileIOJwksProperties(
        @DefaultValue("true")
        boolean enabled,

        @NotBlank
        String activeSignableKid,

        @NotBlank
        @DefaultValue("classpath:keys/")
        String keyBaseLocation,

        @NotEmpty
        Set<@NotBlank String> keys
) {
}
