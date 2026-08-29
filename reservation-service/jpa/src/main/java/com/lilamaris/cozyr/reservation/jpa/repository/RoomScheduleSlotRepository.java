package com.lilamaris.cozyr.reservation.jpa.repository;

import com.lilamaris.cozyr.reservation.domain.RoomScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomScheduleSlotRepository extends JpaRepository<RoomScheduleSlot, UUID> {
}
