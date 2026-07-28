package com.lilamaris.cozyr.identity.resource.server.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.resource-server")
public record ResourceServerProperties(
        @DefaultValue("true")
        @NotNull
        boolean enabled,

        @DefaultValue("gateway:8080/.well-known/jwks.json")
        @NotNull
        URI jwksUri
) {
}
