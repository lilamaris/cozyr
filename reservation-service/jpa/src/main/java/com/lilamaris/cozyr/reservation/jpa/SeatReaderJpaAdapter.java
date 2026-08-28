package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.jpa.repository.SeatRepository;
import com.lilamaris.cozyr.reservation.jpa.row.SeatRow;
import com.lilamaris.cozyr.reservation.jpa.sql.SeatSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatReaderJpaAdapter implements SeatReader {
    private final SeatRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsById(SeatId id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Seat> findById(SeatId id) {
        return repository.findById(id);
    }

    @Override
    public Optional<SeatDetail> findDetailById(SeatId id) {
        var sql = SeatSql.FIND_DETAIL_BY_ID;

        return jdbcClient.sql(sql)
                .param("roomId", id.getRoomId())
                .param("seatId", id.getSeatId())
                .query(SeatRow.Detail.class)
                .optional()
                .map(SeatRow.Detail::toModel);
    }
}
