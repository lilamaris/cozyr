package com.lilamaris.cozyr.identity.kafka;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessagePayload;
import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagePublisherKafkaAdapter implements MessagePublisher {
    private final KafkaTemplate<String, Object> template;

    @Override
    public void publish(MessageEnvelope<? extends MessagePayload> message) {
        var payload = message.payload();
        var topic = payload.kind().canonicalName();
        var key = StringPrecondition.requireNonBlank(message.key(), "key");

        template.send(topic, key, message)
                .whenCompleteAsync((result, exception) -> {
                    if (result != null) {
                        log.info("Send event. result={}", result);
                    }
                    if (exception != null) {
                        log.warn("Failed to send event. topic={}, key={}", topic, key, exception);
                    }
                });
    }
}