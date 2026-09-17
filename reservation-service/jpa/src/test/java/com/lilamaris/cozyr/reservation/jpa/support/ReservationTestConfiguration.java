package com.lilamaris.cozyr.reservation.jpa.support;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.service.CancelReserveService;
import com.lilamaris.cozyr.reservation.application.service.ReserveSeatService;
import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.cozyr.reservation.jdbc.*;
import com.lilamaris.cozyr.reservation.jpa.ReservationJpaAdapter;
import com.lilamaris.cozyr.reservation.jpa.SeatJpaAdapter;
import com.lilamaris.cozyr.reservation.jpa.repository.ReservationRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@EntityScan(basePackageClasses = Reservation.class)
@EnableJpaRepositories(basePackageClasses = ReservationRepository.class)
@Import({ReserveSeatService.class, CancelReserveService.class, ReservationJpaAdapter.class,
        ReservationJdbcAdapter.class, SeatJpaAdapter.class, SeatOccupancyStoreJdbcAdapter.class,
        RoomContextJdbcAdapter.class, RoomScheduleSlotReaderJdbcAdapter.class, DailyUsageJdbcAdapter.class})
public class ReservationTestConfiguration {
    public static final Instant NOW = Instant.parse("2026-09-12T00:00:00Z");

    @Bean
    Clock clock() {
        return Clock.fixed(NOW, ZoneOffset.UTC);
    }

    @Bean
    IdGenerator<UUID> idGenerator() {
        return UUID::randomUUID;
    }

    @Bean
    MessagePublisher messagePublisher() {
        return message -> {
        };
    }
}
