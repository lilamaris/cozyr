package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.jpa.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatReaderJpaAdapter implements SeatReader {
    private final SeatRepository repository;

    @Override
    public boolean existsById(SeatId id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Seat> findById(SeatId id) {
        return repository.findById(id);
    }
}
