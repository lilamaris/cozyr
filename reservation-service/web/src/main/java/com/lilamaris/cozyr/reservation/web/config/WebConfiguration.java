package com.lilamaris.cozyr.reservation.web.config;

import com.lilamaris.cozyr.kernel.web.response.*;
import com.lilamaris.cozyr.reservation.web.advice.ReservationErrorStatusResolver;
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
        return new ReservationErrorStatusResolver();
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
