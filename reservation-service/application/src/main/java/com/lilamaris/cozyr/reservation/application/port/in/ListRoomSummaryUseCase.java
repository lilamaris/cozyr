package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListRoomSummaryQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListRoomSummaryUseCase {
    CursorResult<RoomSummary, RoomCursor> list(ListRoomSummaryQuery query);
}
