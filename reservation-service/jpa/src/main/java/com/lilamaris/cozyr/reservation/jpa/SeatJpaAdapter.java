package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.port.out.SeatReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatStore;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.jpa.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatJpaAdapter implements SeatStore, SeatReader {
    private final SeatRepository repository;

    @Override
    public boolean existsByLocator(SeatLocator seatLocator) {
        return repository.existsByLocator(seatLocator);
    }

    @Override
    public Seat save(Seat seat) {
        return repository.save(seat);
    }
}
