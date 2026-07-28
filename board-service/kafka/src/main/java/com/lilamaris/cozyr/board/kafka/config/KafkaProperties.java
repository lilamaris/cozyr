package com.lilamaris.cozyr.board.kafka.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cozyr.board-service.kafka")
public record KafkaProperties(
        @DefaultValue("true")
        @NotNull
        boolean enabled
) {
}
