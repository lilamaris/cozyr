package com.lilamaris.cozyr.reservation.application.internal;

import com.lilamaris.cozyr.reservation.application.config.ApplicationProperties;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RoomPolicyFactory {
    private final ApplicationProperties properties;

    public RoomOpPolicy fromProperties(long roomId, Instant roomCreatedAt) {
        var roomProperties = properties.room();
        return RoomOpPolicy.of(
                roomId,
                roomProperties.maxReservationPerUserPerDay(),
                roomProperties.maxReservationPerUserPerDay(),
                roomCreatedAt
        );
    }
}
