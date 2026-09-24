package com.lilamaris.cozyr.statistics.web.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "cozyr.web")
public record WebProperties(
        @NotNull
        URI baseUrl
) {
}
