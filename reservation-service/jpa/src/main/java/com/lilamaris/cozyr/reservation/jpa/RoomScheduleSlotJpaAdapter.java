package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotStore;
import com.lilamaris.cozyr.reservation.domain.RoomScheduleSlot;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomScheduleSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomScheduleSlotJpaAdapter implements RoomScheduleSlotStore {
    private final RoomScheduleSlotRepository repository;

    @Override
    public List<RoomScheduleSlot> saveAll(Iterable<RoomScheduleSlot> slots) {
        return repository.saveAll(slots).stream().toList();
    }
}
