package com.lilamaris.cozyr.statistics.application.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;

@Validated
@ConfigurationProperties
public record ApplicationProperties(
        @NotNull
        @DefaultValue("UTC")
        ZoneId timezone
) {
}
