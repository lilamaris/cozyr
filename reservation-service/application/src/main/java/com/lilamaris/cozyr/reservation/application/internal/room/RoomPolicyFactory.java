package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.config.RoomProperties;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RoomPolicyFactory {
    private final RoomProperties properties;

    public RoomOpPolicy fromProperties(RoomId roomId, Instant roomCreatedAt) {
        return RoomOpPolicy.of(
                roomId,
                properties.maxReservationPerUserPerDay(),
                properties.maxSchedulePerReservation(),
                roomCreatedAt
        );
    }
}
