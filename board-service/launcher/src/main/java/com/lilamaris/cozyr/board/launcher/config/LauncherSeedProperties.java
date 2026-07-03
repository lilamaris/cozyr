package com.lilamaris.cozyr.board.launcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cozyr.board.launcher.seed")
public record LauncherSeedProperties(
        @DefaultValue("false")
        boolean enabled
) {
}
