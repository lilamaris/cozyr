package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatId {
    @Column(name = "room_id", nullable = false)
    private long roomId;

    @Column(name = "seat_id", nullable = false)
    private String seatId;

    private SeatId(long roomId, String seatId) {
        this.roomId = NumberPrecondition.requireNonNegative(roomId, "roomId");
        this.seatId = StringPrecondition.requireNonBlank(seatId, "seatId");
    }

    public static SeatId of(long roomId, String seatId) {
        return new SeatId(roomId, seatId);
    }
}
