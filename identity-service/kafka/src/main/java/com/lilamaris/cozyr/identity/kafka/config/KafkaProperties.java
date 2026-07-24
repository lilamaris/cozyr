package com.lilamaris.cozyr.identity.kafka.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cozyr.identity-service.kafka")
public record KafkaProperties(
        @DefaultValue("true")
        @NotNull
        boolean enabled
) {
}
