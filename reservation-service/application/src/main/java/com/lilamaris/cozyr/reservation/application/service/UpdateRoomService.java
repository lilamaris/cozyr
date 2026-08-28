package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.in.UpdateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.UpdateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomUpdatedResult;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class UpdateRoomService implements UpdateRoomUseCase {
    private final RoomReader reader;
    private final Clock clock;

    @Override
    @Transactional
    public RoomUpdatedResult update(UpdateRoomCommand command) {
        var roomId = command.roomId();
        var room = reader.findById(roomId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));

        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        room.updateName(name, now);
        room.updateDescription(description, now);

        return RoomUpdatedResult.from(room);
    }
}
