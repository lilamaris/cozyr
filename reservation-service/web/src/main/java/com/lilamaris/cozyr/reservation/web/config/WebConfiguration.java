package com.lilamaris.cozyr.reservation.web.config;

import com.lilamaris.cozyr.kernel.web.response.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebConfiguration {
    @Bean
    public ErrorCodeResolver errorCodeResolver() {
        return new DefaultErrorCodeResolver();
    }

    @Bean
    public ErrorStatusResolver errorStatusResolver() {
        return new DefaultErrorStatusResolver();
    }

    @Bean
    public ErrorTypeUriResolver errorTypeUriResolver(WebProperties properties) {
        return new DefaultErrorTypeUriResolver(properties.baseUrl());
    }

    @Bean
    public ProblemDetailFactory problemDetailFactory(
            ErrorCodeResolver errorCodeResolver,
            ErrorStatusResolver errorStatusResolver,
            ErrorTypeUriResolver errorTypeUriResolver
    ) {
        return new ProblemDetailFactory(errorCodeResolver, errorStatusResolver, errorTypeUriResolver);
    }
}
