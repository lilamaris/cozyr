package com.lilamaris.cozyr.reservation.application.config;

import com.lilamaris.cozyr.reservation.application.model.schedule.ScheduleFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {
    @Bean
    Clock clock(ApplicationProperties properties) {
        return Clock.system(properties.timezone());
    }

    @Bean
    ScheduleFactory scheduleFactory() {
        return new ScheduleFactory();
    }
}
