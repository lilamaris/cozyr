package com.lilamaris.cozyr.reservation.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationServiceProgressCode implements ApplicationProgressCode {
    ROOM_NOT_FOUND(ProcessReason.REJECTED, "room", "not-found", "방을 찾을 수 없습니다."),
    SEAT_NOT_FOUND(ProcessReason.REJECTED, "seat", "not-found", "좌석을 찾을 수 없습니다."),

    SEAT_ID_DUPLICATED(ProcessReason.REJECTED, "seat", "duplicated", "동일한 좌석이 이미 존재합니다.");

    private final ProcessReason reason;
    private final String resourceName;
    private final String type;
    private final String message;

    @Override
    public ProcessReason reason() {
        return reason;
    }

    @Override
    public String resourceName() {
        return resourceName;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String message() {
        return message;
    }
}
