package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.internal.room.RoomInternalUpdateService;
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
    private final RoomInternalUpdateService roomInternalUpdateService;
    private final RoomReader roomReader;
    private final Clock clock;

    @Override
    @Transactional
    public RoomUpdatedResult update(UpdateRoomCommand command) {
        var now = clock.instant();

        var roomId = command.roomId();
        var userId = command.userId();
        var params = command.params();
        roomInternalUpdateService.update(roomId, params, userId, now);

        return roomReader.findById(roomId)
                .map(RoomUpdatedResult::from)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));
    }
}
