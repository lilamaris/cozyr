package com.lilamaris.cozyr.statistics.web.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "cozyr.statistics-service.web")
public record WebProperties(
        @DefaultValue("true")
        boolean enabled,

        @NotNull
        URI baseUrl
) {
}
