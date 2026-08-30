package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomFilter;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface RoomSummaryReader {
    CursorResult<RoomSummary, RoomCursor> find(RoomFilter filter, CursorRequest<RoomCursor> request);
}
