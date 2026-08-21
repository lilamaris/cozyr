package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.application.port.in.ListRoomSummaryUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListRoomSummaryQuery;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListRoomSummaryService implements ListRoomSummaryUseCase {
    private final RoomReader reader;

    @Override
    public CursorResult<RoomSummary, RoomCursor> list(ListRoomSummaryQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findSummaries(filter, request);
    }
}
