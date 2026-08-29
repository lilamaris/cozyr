package com.lilamaris.cozyr.reservation.application.internal;

import com.lilamaris.cozyr.reservation.application.config.ApplicationProperties;
import com.lilamaris.cozyr.reservation.application.model.schedule.ScheduleFactory;
import com.lilamaris.cozyr.reservation.domain.RoomScheduleSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomScheduleSlotFactory {
    private final ApplicationProperties properties;
    private final ScheduleFactory scheduleFactory;

    public List<RoomScheduleSlot> fromProperties(long roomId) {
        var props = properties.room();
        var steps = Duration.ofMinutes(props.slotMinute());
        var schedules = scheduleFactory.create(props.openTime(), props.closeTime(), steps);

        return schedules.stream()
                .map(schedule -> RoomScheduleSlot.of(roomId, schedule.from(), schedule.to()))
                .toList();
    }
}