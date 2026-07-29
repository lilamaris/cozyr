package com.lilamaris.cozyr.identity.web.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.web")
public record WebProperties(
        @DefaultValue("true")
        boolean enabled,

        @NotBlank
        String baseUrl
) {
}
