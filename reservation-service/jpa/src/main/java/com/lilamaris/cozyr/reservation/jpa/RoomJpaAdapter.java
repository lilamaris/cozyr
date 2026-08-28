package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.jpa.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomJpaAdapter implements RoomStore, RoomReader {
    private final RoomRepository repository;

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Room> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }
}
