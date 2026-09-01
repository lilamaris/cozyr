package com.lilamaris.cozyr.reservation.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationServiceProgressCode implements ApplicationProgressCode {
    ROOM_NOT_FOUND(ProcessReason.REJECTED, "room", "not-found", "방을 찾을 수 없습니다."),
    SEAT_NOT_FOUND(ProcessReason.REJECTED, "seat", "not-found", "좌석을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(ProcessReason.REJECTED, "reservation", "not-found", "예약을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(ProcessReason.REJECTED, "schedule", "not-found", "예약 가능한 시간을 찾을 수 없습니다."),

    SEAT_ID_DUPLICATED(ProcessReason.REJECTED, "seat", "duplicated", "동일한 좌석이 이미 존재합니다."),

    SCHEDULE_ALREADY_OCCUPIED(ProcessReason.REJECTED, "reservation", "duplicated", "해당 시간의 좌석은 이미 예약되었습니다."),
    RESERVATION_ALREADY_CANCELED(ProcessReason.REJECTED, "reservation", "duplicated", "해당 예약은 이미 취소되었습니다."),

    MAX_RESERVABLE_COUNT_EXCEEDED(ProcessReason.REJECTED, "reservation", "daily-reservation-limit-exceeded", "해당 방의 일일 예약 가능 횟수를 초과했습니다."),
    MAX_SCHEDULE_COUNT_EXCEEDED(ProcessReason.REJECTED, "reservation", "schedule-count-limit-exceeded", "한 번에 예약 가능한 스케쥴 슬롯 수를 초과했습니다.");

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
