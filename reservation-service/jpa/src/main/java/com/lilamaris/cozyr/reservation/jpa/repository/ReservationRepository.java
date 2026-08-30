package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
