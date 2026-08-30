package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.SeatOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatOccupancyRepository extends JpaRepository<SeatOccupancy, UUID> {
}
