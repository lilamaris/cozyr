package com.lilamaris.cozyr.identity.io.config;

import com.lilamaris.cozyr.identity.io.JwksReaderIOAdapter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.identity-service.jwks.io",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(FileIOJwksProperties.class)
public class FileIOJwksAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    JwksReaderIOAdapter jwksReaderIOAdapter(
            FileIOJwksProperties properties,
            ResourceLoader loader
    ) {
        return new JwksReaderIOAdapter(properties, loader);
    }
}
