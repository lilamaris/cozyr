package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.internal.id.IdGenerator;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.application.service.ReserveSeatService;
import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.cozyr.reservation.domain.ReservationId;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.jdbc.DailyUsageJdbcAdapter;
import com.lilamaris.cozyr.reservation.jdbc.RoomContextJdbcAdapter;
import com.lilamaris.cozyr.reservation.jdbc.RoomScheduleSlotReaderJdbcAdapter;
import com.lilamaris.cozyr.reservation.jdbc.SeatOccupancyStoreJpaAdapter;
import com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion;
import com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion;
import com.lilamaris.cozyr.reservation.jpa.repository.ReservationRepository;
import com.lilamaris.cozyr.reservation.jpa.support.DailyUsageTestSupport;
import com.lilamaris.cozyr.reservation.jpa.support.RoomTestSupport;
import com.lilamaris.cozyr.reservation.jpa.support.UserTestSupport;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

import static com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion.assertReservationByRoomThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion.assertReservationThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion.assertActiveOccupanciesThat;
import static com.lilamaris.cozyr.reservation.jpa.support.DailyUsageTestSupport.assertDailyReservationUsageThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.jpa.show-sql=false",
        "spring.flyway.enabled=true"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ReserveSeatConcurrencyTest.TestConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Reserve Seat 동시성 테스트")
class ReserveSeatConcurrencyTest {
    // V4 uses uuidv7(), which requires PostgreSQL 18.
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18.3-alpine");
    private static final Instant NOW = Instant.parse("2026-09-12T00:00:00Z");
    private static final LocalDate DATE = LocalDate.of(2026, 9, 13);
    private UserTestSupport.TestContext userContext;
    private RoomTestSupport.TestContext roomContext;

    @Autowired
    private ReserveSeatUseCase reserveSeatUseCase;

    @Autowired
    private JdbcClient jdbcClient;

    @DynamicPropertySource
    static void database(DynamicPropertyRegistry registry) {
        POSTGRES.start();
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeAll
    void setup() {
        userContext = UserTestSupport.createContext(jdbcClient, NOW);
        roomContext = RoomTestSupport.createContext(jdbcClient, NOW);
    }

    @AfterAll
    void cleanup() {
        try {
            UserTestSupport.cleanup(jdbcClient, userContext);
            RoomTestSupport.cleanup(jdbcClient, roomContext);
        } finally {
            POSTGRES.stop();
        }
    }

    @Test
    @DisplayName("서로 다른 두 사용자가 같은 좌석·날짜·시간을 동시에 예약하면 한 건만 커밋된다")
    void onlyOneReservationCommitsForTheSameSeatAndSlot() throws Exception {
        var firstCommand = ReserveSeatCommand.of(userContext.firstUserId(), roomContext.targetSeatId(), DATE, Set.of(roomContext.slotId()));
        var secondCommand = ReserveSeatCommand.of(userContext.secondUserId(), roomContext.targetSeatId(), DATE, Set.of(roomContext.slotId()));

        List<Outcome> outcomes;
        var executor = Executors.newFixedThreadPool(2);
        try {
            var first = executor.submit(() -> reserve(firstCommand));
            var second = executor.submit(() -> reserve(secondCommand));
            outcomes = List.of(first.get(30, TimeUnit.SECONDS), second.get(30, TimeUnit.SECONDS));
        } finally {
            executor.shutdownNow();
            assertThat(executor.awaitTermination(15, TimeUnit.SECONDS))
                    .as("reservation workers must terminate")
                    .isTrue();
        }

        var successes = outcomes.stream().filter(outcome -> outcome.result() != null).toList();
        var failures = outcomes.stream().filter(outcome -> outcome.failure() != null).toList();
        assertThat(successes).hasSize(1);
        assertThat(failures).hasSize(1);
        assertThat(failures.getFirst().failure().getApplicationCode())
                .isEqualTo(ReservationServiceProgressCode.SCHEDULE_ALREADY_OCCUPIED);

        var winner = successes.getFirst().result();

        assertThat(winner.reserveUserId())
                .isIn(userContext.firstUserId(), userContext.secondUserId());

        assertReservationByRoomThat(jdbcClient, roomContext.roomId())
                .extracting(ReservationAssertion.ReservationAssertRow::id)
                .containsExactly(winner.reservationId());

        assertReservationThat(jdbcClient, winner.reservationId())
                .isNotEmpty()
                .hasSeatId(roomContext.targetSeatId())
                .hasStatus(ReservationStatus.RESERVED)
                .hasOccupancyDate(DATE)
                .hasReservedUserId(winner.reserveUserId());

        assertActiveOccupanciesThat(jdbcClient, roomContext.targetSeatId(), DATE)
                .extracting(
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::reservationId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::slotId
                )
                .containsExactly(tuple(winner.reservationId(), roomContext.slotId()));

        assertDailyReservationUsageThat(jdbcClient, roomContext.roomId(), DATE)
                .extracting(DailyUsageTestSupport.DailyReservationUsageAssertRow::userId)
                .containsExactly(winner.reserveUserId());

        assertDailyReservationUsageThat(jdbcClient, winner.reserveUserId(), roomContext.roomId(), DATE)
                .hasCount(1);
    }

    private Outcome reserve(ReserveSeatCommand command) {
        try {
            return new Outcome(reserveSeatUseCase.reserve(command), null);
        } catch (ApplicationException exception) {
            return new Outcome(null, exception);
        }
    }

    private record Outcome(ReserveSeatResult result, ApplicationException failure) {
    }

    @Configuration(proxyBeanMethods = false)
    @EntityScan(basePackageClasses = Reservation.class)
    @EnableJpaRepositories(basePackageClasses = ReservationRepository.class)
    @Import({ReserveSeatService.class, ReservationJpaAdapter.class, SeatJpaAdapter.class,
            RoomContextJdbcAdapter.class, RoomScheduleSlotReaderJdbcAdapter.class, DailyUsageJdbcAdapter.class})
    static class TestConfig {
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

        @Bean
        SeatOccupancyStore seatOccupancyStore(JdbcClient jdbc) {
            var delegate = new SeatOccupancyStoreJpaAdapter(jdbc);
            var barrier = new CyclicBarrier(2);
            return new SeatOccupancyStore() {
                @Override
                public boolean tryOccupy(ReservationId reservationId, LocalDate date, SeatId seatId, Set<UUID> slotIds) {
                    assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isTrue();
                    try {
                        barrier.await(10, TimeUnit.SECONDS);
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Interrupted waiting for the competing reservation", exception);
                    } catch (BrokenBarrierException | TimeoutException exception) {
                        throw new IllegalStateException("Both reservations must reach the occupancy boundary", exception);
                    }
                    return delegate.tryOccupy(reservationId, date, seatId, slotIds);
                }

                @Override
                public boolean tryRelease(ReservationId reservationId, Instant releasedAt) {
                    return delegate.tryRelease(reservationId, releasedAt);
                }
            };
        }
    }
}
