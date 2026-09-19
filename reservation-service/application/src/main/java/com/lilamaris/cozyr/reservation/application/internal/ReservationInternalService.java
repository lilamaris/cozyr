package com.lilamaris.cozyr.reservation.application.internal;

import com.lilamaris.cozyr.reservation.application.config.ApplicationProperties;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import com.lilamaris.cozyr.reservation.application.port.out.*;
import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.cozyr.reservation.domain.ReservationId;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationInternalService {
    private final RoomPolicyStore roomPolicyStore;
    private final RoomScheduleSlotReader roomScheduleSlotReader;
    private final SeatReader seatReader;
    private final SeatOccupancyStore seatOccupancyStore;
    private final DailyUsageCounter dailyUsageCounter;
    private final ReservationStore reservationStore;
    private final IdGenerator<UUID> idGenerator;
    private final ApplicationProperties properties;

    public ReserveSeatResult reserve(SeatLocator seatLocator, LocalDate reservationDate, Set<UUID> slotIds, UUID reservationUserId, Instant reservedAt) {
        // start fast-fail
        var seatExists = seatReader.existsByLocator(seatLocator);
        if (!seatExists) throw new ApplicationException(ReservationServiceProgressCode.SEAT_NOT_FOUND);

        if (slotIds.isEmpty()) throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_NOT_FOUND);
        var targetSlots = roomScheduleSlotReader.findAllByRoomId(seatLocator.roomId(), slotIds).stream()
                .collect(Collectors.toUnmodifiableSet());
        if (slotIds.size() != targetSlots.size())
            throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_NOT_FOUND);
        // end fast-fail

        var latestEndAt = targetSlots.stream()
                .map(slot -> reservationDate
                        .atTime(slot.endAt())
                        .atZone(properties.timezone())
                        .toInstant()
                )
                .max(Comparator.naturalOrder())
                .orElseThrow();

        var earliestStartAt = targetSlots.stream()
                .map(slot -> reservationDate
                        .atTime(slot.startAt())
                        .atZone(properties.timezone())
                        .toInstant()
                )
                .min(Comparator.naturalOrder())
                .orElseThrow();

        var reservationId = ReservationId.of(idGenerator.generate());

        var roomOpPolicy = roomPolicyStore.findForShare(seatLocator.roomId())
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));

        if (!roomOpPolicy.allowsScheduleCount(slotIds.size())) throw new ApplicationException(ReservationServiceProgressCode.MAX_SCHEDULE_COUNT_EXCEEDED);
        if (!roomOpPolicy.allowsReservation(earliestStartAt, latestEndAt))
            throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_OUTSIDE_OPERATING_HOURS);

        var counted = dailyUsageCounter.tryIncrease(reservationUserId, seatLocator.roomId(), reservationDate, roomOpPolicy.getMaxReservationPerUserPerDay());
        if (!counted) throw new ApplicationException(ReservationServiceProgressCode.MAX_RESERVABLE_COUNT_EXCEEDED);

        var occupied = seatOccupancyStore.tryOccupy(reservationId, reservationDate, seatLocator, slotIds);
        if (!occupied) throw new ApplicationException(ReservationServiceProgressCode.SCHEDULE_ALREADY_OCCUPIED);

        var reservation = Reservation.of(reservationId, reservationUserId, seatLocator.roomId(), seatLocator.seatId(), reservationDate, reservedAt);
        reservationStore.save(reservation);

        return ReserveSeatResult.of(
                reservationId.getValue(),
                reservationDate,
                seatLocator,
                reservationUserId,
                reservedAt,
                targetSlots
        );
    }
}
