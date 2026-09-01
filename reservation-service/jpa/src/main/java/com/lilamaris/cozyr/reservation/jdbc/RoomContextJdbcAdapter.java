package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.model.room.RoomContext;
import com.lilamaris.cozyr.reservation.application.port.out.RoomContextReader;
import com.lilamaris.cozyr.reservation.jdbc.row.RoomContextRow;
import com.lilamaris.cozyr.reservation.jdbc.sql.RoomContextSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomContextJdbcAdapter implements RoomContextReader {
    private final JdbcClient jdbcClient;

    @Override
    public Optional<RoomContext> findByRoomId(Long roomId) {
        var sql = RoomContextSql.FIND_BY_ROOM_ID;

        return jdbcClient.sql(sql)
                .param("roomId", roomId)
                .query(RoomContextRow.class)
                .optional()
                .map(RoomContextRow::toModel);
    }
}
