package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.config.ApplicationProperties;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RoomPolicyFactory {
    private final ApplicationProperties properties;

    public RoomOpPolicy fromProperties(RoomId roomId, Instant roomCreatedAt) {
        var roomProperties = properties.room();
        return RoomOpPolicy.of(
                roomId,
                roomProperties.maxReservationPerUserPerDay(),
                roomProperties.maxSchedulePerReservation(),
                roomCreatedAt
        );
    }
}
