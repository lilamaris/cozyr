package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "room_schedule_slot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private long roomId;

    @Column(name = "start_at", nullable = false)
    private LocalTime start_at;

    @Column(name = "end_at", nullable = false)
    private LocalTime end_at;

    private RoomScheduleSlot(long roomId, LocalTime start_at, LocalTime end_at) {
        this.roomId = NumberPrecondition.requireNonNegative(roomId, "roomId");
        this.start_at = ObjectPrecondition.requireNonNull(start_at, "start_at");
        this.end_at = ObjectPrecondition.requireNonNull(end_at, "end_at");
    }

    public static RoomScheduleSlot of(long roomId, LocalTime start_at, LocalTime end_at) {
        return new RoomScheduleSlot(roomId, start_at, end_at);
    }
}