package com.lilamaris.cozyr.identity.kafka;

import com.lilamaris.cozyr.identity.application.port.out.EventPublisher;
import com.lilamaris.cozyr.identity.contract.event.EventPayload;
import com.lilamaris.cozyr.identity.contract.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisherKafkaAdapter implements EventPublisher {
    private final TopicResolver topicResolver;
    private final KafkaTemplate<String, Object> template;

    @Override
    public void publish(EventType eventType, EventPayload<?> payload) {
        var topic = topicResolver.resolve(eventType);
        var key = payload.id().toString();

        template.send(topic, key, payload)
                .whenCompleteAsync((result, exception) -> {
                    if (exception != null) {
                        log.warn("Failed to send event. topic={}, key={}", topic, key, exception);
                    }
                });
    }
}
