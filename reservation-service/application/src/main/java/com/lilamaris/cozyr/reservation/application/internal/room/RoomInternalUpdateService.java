package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.config.ApplicationProperties;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.out.ReservableScheduleReader;
import com.lilamaris.cozyr.reservation.application.port.out.RoomOwnerStore;
import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateParams;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomInternalUpdateService {
    private final ReservableScheduleReader reservableScheduleReader;
    private final RoomOwnerStore roomOwnerStore;
    private final RoomPolicyStore roomPolicyStore;
    private final ApplicationProperties properties;

    public boolean update(RoomId roomId, RoomUpdateParams params, UUID userId, Instant updatedAt) {
        var status = roomOwnerStore.updateByOwned(roomId, userId, params, updatedAt);

        return switch (status) {
            case UPDATED -> true;
            case NOT_OWNED -> throw new ApplicationException(ReservationServiceProgressCode.FORBIDDEN);
        };
    }

    public boolean activate(RoomId roomId, UUID userId, Instant activatedAt, Instant updatedAt) {
        var policy = roomPolicyStore.findForUpdate(roomId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));

        var updateStatus = roomPolicyStore.updateActivatedAtWithAwareByOwner(roomId, userId, activatedAt, updatedAt);
        if (updateStatus == RoomUpdateStatus.NOT_OWNED) throw new ApplicationException(ReservationServiceProgressCode.FORBIDDEN);

        return true;
    }

    public boolean deactivate(RoomId roomId, UUID userId, Instant deactivatedAt, Instant updatedAt) {
        var policy = roomPolicyStore.findForUpdate(roomId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));

        var ableAt = reservableScheduleReader.findLatestReservedEndAtByRoomId(roomId, updatedAt, properties.timezone())
                .orElse(updatedAt);

        if (deactivatedAt.isBefore(ableAt))
            throw new ApplicationException(ReservationServiceProgressCode.ROOM_DEACTIVATION_TOO_EARLY);

        var updateStatus = roomPolicyStore.updateDeactivatedAtWithAwareByOwner(roomId, userId, deactivatedAt, updatedAt);
        if (updateStatus == RoomUpdateStatus.NOT_OWNED) throw new ApplicationException(ReservationServiceProgressCode.FORBIDDEN);

        return true;
    }
}
