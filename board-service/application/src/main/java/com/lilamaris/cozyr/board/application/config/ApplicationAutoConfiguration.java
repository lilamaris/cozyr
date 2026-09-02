package com.lilamaris.cozyr.board.application.config;

import com.lilamaris.cozyr.board.contract.provider.BoardServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.board.application",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationAutoConfiguration {
    @Bean
    Clock clock(ApplicationProperties properties) {
        return Clock.system(properties.timezone());
    }

    @Bean
    ServiceDescriptor serviceDescriptor() {
        return new BoardServiceDescriptor();
    }
}
