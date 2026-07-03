package com.lilamaris.cozyr.board.launcher.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.board.launcher.seed",
        name = "enabled",
        havingValue = "true"
)
@EnableConfigurationProperties(LauncherSeedProperties.class)
public class LauncherSeedAutoConfiguration {

}
