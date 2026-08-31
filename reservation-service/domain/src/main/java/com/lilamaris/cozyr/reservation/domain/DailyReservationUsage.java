package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_reservation_usage")
public class DailyReservationUsage {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private final UUID userId;

    @Column(name = "room_id", nullable = false)
    private final Long roomId;

    @Column(name = "reservation_date", nullable = false)
    private final LocalDate reservationDate;

    @Column(name = "reservation_count", nullable = false)
    private final int reservationCount;

    private DailyReservationUsage(UUID userId, Long roomId, LocalDate reservationDate, int reservationCount) {
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.roomId = NumberPrecondition.requireNonNegative(roomId, "roomId");
        this.reservationDate = ObjectPrecondition.requireNonNull(reservationDate, "reservationDate");
        this.reservationCount = NumberPrecondition.requireNonNegative(reservationCount, "reservationCount");
    }

    public static DailyReservationUsage of(UUID userId, Long roomId, LocalDate reservationDate, int reservationCount) {
        return new DailyReservationUsage(userId, roomId, reservationDate, reservationCount);
    }
}
