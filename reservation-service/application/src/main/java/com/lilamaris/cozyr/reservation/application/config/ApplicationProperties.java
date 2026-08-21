package com.lilamaris.cozyr.reservation.application.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;

@Validated
@ConfigurationProperties(prefix = "cozyr.reservation-service.application")
public record ApplicationProperties(
        @DefaultValue("UTC")
        @NotNull
        ZoneId timezone
) {
}
