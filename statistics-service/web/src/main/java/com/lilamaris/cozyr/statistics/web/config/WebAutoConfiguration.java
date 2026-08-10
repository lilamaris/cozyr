package com.lilamaris.cozyr.statistics.web.config;

import com.lilamaris.cozyr.kernel.web.response.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "cozyr.statistics-service.web",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ErrorCodeResolver errorCodeResolver() {
        return new DefaultErrorCodeResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorStatusResolver errorStatusResolver() {
        return new DefaultErrorStatusResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorTypeUriResolver errorTypeUriResolver(WebProperties properties) {
        return new DefaultErrorTypeUriResolver(properties.baseUrl());
    }

    @Bean
    @ConditionalOnMissingBean
    public ProblemDetailFactory problemDetailFactory(
            ErrorCodeResolver errorCodeResolver,
            ErrorStatusResolver errorStatusResolver,
            ErrorTypeUriResolver errorTypeUriResolver
    ) {
        return new ProblemDetailFactory(errorCodeResolver, errorStatusResolver, errorTypeUriResolver);
    }
}
