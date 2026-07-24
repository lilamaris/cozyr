package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.contract.event.EventPayload;
import com.lilamaris.cozyr.identity.contract.event.EventType;

public interface EventPublisher {
    void publish(EventType eventType, EventPayload<?> payload);
}
