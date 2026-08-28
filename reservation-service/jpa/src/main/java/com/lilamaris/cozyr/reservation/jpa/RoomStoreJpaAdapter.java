package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStoreJpaAdapter implements RoomStore {
    private final RoomRepository repository;

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }
}
