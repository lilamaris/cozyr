package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.message.MessageKind;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationServiceMessageKind implements MessageKind {
    ROOM_CREATED("room.created", 1),
    SEAT_CREATED("seat.created", 1),
    RESERVATION_CREATED("reservation.created", 1),
    RESERVATION_CANCELED("reservation.canceled", 1);

    private final String canonicalName;
    private final int version;

    @Override
    public String canonicalName() {
        return canonicalName;
    }

    @Override
    public int version() {
        return version;
    }
}
