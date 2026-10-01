package com.lilamaris.cozyr.board.application.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;

@Validated
@ConfigurationProperties(prefix = "cozyr.application")
public record ApplicationProperties(
        @DefaultValue("UTC")
        @NotNull
        ZoneId timezone
) {
}
