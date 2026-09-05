package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotReader;
import com.lilamaris.cozyr.reservation.jdbc.sql.RoomScheduleSlotSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomScheduleSlotReaderJdbcAdapter implements RoomScheduleSlotReader {
    private final JdbcClient jdbcClient;

    @Override
    public List<RoomSchedule> findAllByRoomId(long roomId, Collection<UUID> scheduleSlotIds) {
        var sql = RoomScheduleSlotSql.FIND_ALL_BY_SLOT_IDS;

        return jdbcClient.sql(sql)
                .param("roomId", roomId)
                .param("scheduleSlotIds", scheduleSlotIds)
                .query(RoomSchedule.class)
                .list();
    }
}
