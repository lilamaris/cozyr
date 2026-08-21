package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.model.room.RoomFilter;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

import java.util.Optional;

public interface RoomReader {
    boolean existsById(long id);

    Optional<Room> findById(long id);

    CursorResult<RoomSummary, RoomCursor> findSummaries(RoomFilter filter, CursorRequest<RoomCursor> request);

    Optional<RoomDetail> findDetailById(long id);
}
