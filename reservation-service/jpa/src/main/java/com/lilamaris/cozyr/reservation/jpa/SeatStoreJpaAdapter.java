package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.SeatStore;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.jpa.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatStoreJpaAdapter implements SeatStore {
    private final SeatRepository repository;

    @Override
    public Seat save(Seat seat) {
        return repository.save(seat);
    }
}
