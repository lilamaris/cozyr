package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.time.LocalDate;
import java.util.UUID;

public interface DailyUsageCounter {
    boolean tryIncrease(UUID userId, RoomId roomId, LocalDate reservationDate, int maxCount);

    boolean tryDecrease(UUID userId, RoomId roomId, LocalDate reservationDate);
}
