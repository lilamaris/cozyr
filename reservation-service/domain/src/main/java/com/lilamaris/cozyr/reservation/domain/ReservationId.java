package com.lilamaris.cozyr.reservation.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationId {
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    private UUID value;

    private ReservationId(UUID value) {
        this.value = ObjectPrecondition.requireNonNull(value, "value");
    }

    public static ReservationId of(UUID id) {
        return new ReservationId(id);
    }
}
