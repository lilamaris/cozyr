package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.out.ReservationReader;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationStore;
import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.cozyr.reservation.jpa.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationJpaAdapter implements ReservationStore, ReservationReader {
    private final ReservationRepository repository;

    @Override
    public boolean existsById(UUID reservationId) {
        return repository.existsById(reservationId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }
}
