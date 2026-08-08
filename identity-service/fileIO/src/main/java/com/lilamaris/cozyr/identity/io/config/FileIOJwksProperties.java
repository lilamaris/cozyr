package com.lilamaris.cozyr.identity.io.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.jwks.io")
public record FileIOJwksProperties(
        @DefaultValue("true")
        boolean enabled,

        @NotBlank
        String activeSignableKid,

        @NotNull
        Path keyBasePath,

        @NotBlank
        @DefaultValue("public.pem")
        String publicKeyPrefix,

        @NotBlank
        @DefaultValue("private.pem")
        String privateKeyPrefix
) {
}
