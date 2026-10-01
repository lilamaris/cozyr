package com.lilamaris.cozyr.reservation.application.internal.room;

import com.lilamaris.cozyr.reservation.application.config.RoomProperties;
import com.lilamaris.cozyr.reservation.application.model.schedule.ScheduleFactory;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomScheduleSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomScheduleSlotFactory {
    private final RoomProperties properties;
    private final ScheduleFactory scheduleFactory;

    public List<RoomScheduleSlot> fromProperties(RoomId roomId) {
        var steps = Duration.ofMinutes(properties.slotMinute());
        var schedules = scheduleFactory.create(properties.openTime(), properties.closeTime(), steps);

        return schedules.stream()
                .map(schedule -> RoomScheduleSlot.of(roomId, schedule.from(), schedule.to()))
                .toList();
    }
}