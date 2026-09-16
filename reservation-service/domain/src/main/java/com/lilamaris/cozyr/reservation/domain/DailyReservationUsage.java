package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_reservation_usage")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyReservationUsage {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "room_id", updatable = false, nullable = false))
    private RoomId roomId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservation_count", nullable = false)
    private int reservationCount;

    private DailyReservationUsage(UUID userId, RoomId roomId, LocalDate reservationDate, int reservationCount) {
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.roomId = ObjectPrecondition.requireNonNull(roomId, "roomId");
        this.reservationDate = ObjectPrecondition.requireNonNull(reservationDate, "reservationDate");
        this.reservationCount = NumberPrecondition.requireNonNegative(reservationCount, "reservationCount");
    }

    public static DailyReservationUsage of(UUID userId, RoomId roomId, LocalDate reservationDate, int reservationCount) {
        return new DailyReservationUsage(userId, roomId, reservationDate, reservationCount);
    }
}
