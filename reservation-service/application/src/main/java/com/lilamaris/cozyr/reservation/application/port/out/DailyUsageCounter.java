package com.lilamaris.cozyr.reservation.application.port.out;

import java.time.LocalDate;
import java.util.UUID;

public interface DailyUsageCounter {
    boolean tryIncrease(UUID userId, Long roomId, LocalDate reservationDate, int maxCount);
}
