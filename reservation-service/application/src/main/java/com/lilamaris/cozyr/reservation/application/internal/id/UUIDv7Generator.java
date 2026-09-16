package com.lilamaris.cozyr.reservation.application.internal.id;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDv7Generator implements IdGenerator<UUID> {
    @Override
    public UUID generate() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
