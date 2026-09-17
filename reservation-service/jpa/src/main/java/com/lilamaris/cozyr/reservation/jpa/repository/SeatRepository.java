package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM Seat s
                WHERE s.roomId = :#{#sl.roomId}
                    AND s.id = :#{#sl.seatId}
            )
            """)
    boolean existsByLocator(@Param("sl") SeatLocator seatLocator);
}
