package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.port.in.CreateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomStore;
import com.lilamaris.cozyr.reservation.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateRoomService implements CreateRoomUseCase {
    private final RoomStore store;
    private final Clock clock;

    @Override
    @Transactional
    public RoomCreatedResult create(CreateRoomCommand command) {
        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        var room = Room.of(name, description, now);
        var saved = store.save(room);

        return RoomCreatedResult.from(saved);
    }
}
