package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.RoomScheduleSlotReader;
import com.lilamaris.cozyr.reservation.jpa.sql.RoomScheduleSlotSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomScheduleSlotReaderJpaAdapter implements RoomScheduleSlotReader {
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsByRoom(long roomId, Collection<UUID> scheduleSlotIds) {
        var sql = RoomScheduleSlotSql.EXISTS_BY_SLOT_IDS;

        return jdbcClient.sql(sql)
                .param("slotCount", scheduleSlotIds.size())
                .param("roomId", roomId)
                .param("scheduleSlotIds", scheduleSlotIds)
                .query(Boolean.class)
                .single();
    }
}
