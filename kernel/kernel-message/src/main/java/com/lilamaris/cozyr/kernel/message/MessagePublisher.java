package com.lilamaris.cozyr.kernel.message;

public interface MessagePublisher {
    void publish(MessageEnvelope<? extends MessagePayload> message);
}
