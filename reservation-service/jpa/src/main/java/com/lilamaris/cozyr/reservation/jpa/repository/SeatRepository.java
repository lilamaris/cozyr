package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
}
