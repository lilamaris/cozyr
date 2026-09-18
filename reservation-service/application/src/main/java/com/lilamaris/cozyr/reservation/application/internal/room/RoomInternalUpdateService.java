package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.out.RoomOwnerStore;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateParams;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomInternalUpdateService {
    private final RoomOwnerStore roomOwnerStore;

    public boolean update(RoomId roomId, RoomUpdateParams params, UUID userId, Instant updatedAt) {
        var status = roomOwnerStore.updateByOwned(roomId, userId, params, updatedAt);

        return switch (status) {
            case UPDATED -> true;
            case NOT_OWNED -> throw new ApplicationException(ReservationServiceProgressCode.FORBIDDEN);
        };
    }
}
