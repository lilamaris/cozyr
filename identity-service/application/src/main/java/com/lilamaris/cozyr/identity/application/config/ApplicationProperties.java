package com.lilamaris.cozyr.identity.application.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.ZoneId;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.application")
public record ApplicationProperties(
        @DefaultValue("true")
        boolean enabled,

        @DefaultValue("UTC")
        @NotNull
        ZoneId timezone,

        @DefaultValue("cozyr")
        @NotBlank
        String issuer,

        @DefaultValue("PT15M")
        @NotNull
        Duration expiration
) {
}
