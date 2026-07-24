package com.lilamaris.cozyr.identity.kafka;

import com.lilamaris.cozyr.identity.contract.event.EventType;

public class TopicResolver {
    private static final String TOPIC_SEPARATOR = ".";

    public String resolve(EventType eventType) {
        return eventType.getResource() + TOPIC_SEPARATOR + eventType.getAction();
    }
}
