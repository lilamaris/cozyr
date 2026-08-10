package com.lilamaris.cozyr.statistics.kafka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();

        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
