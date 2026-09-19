package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.internal.room.RoomInternalUpdateService;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.DeactivateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.command.DeactivateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import com.lilamaris.cozyr.reservation.application.service.UpdateRoomService;
import com.lilamaris.cozyr.reservation.jdbc.RoomJdbcAdapter;
import com.lilamaris.cozyr.reservation.jdbc.ReservableScheduleReaderJdbcAdapter;
import com.lilamaris.cozyr.reservation.jpa.support.ControlledRoomOpPolicyStore;
import com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion;
import com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.lilamaris.cozyr.reservation.jpa.repository.RoomOpPolicyRepository;
import com.lilamaris.cozyr.reservation.jpa.support.ReservationTestConfiguration;
import com.lilamaris.cozyr.reservation.jpa.support.RoomTestSupport;
import com.lilamaris.cozyr.reservation.jpa.support.UserTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion.assertReservationByRoomThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion.assertActiveOccupanciesThat;
import static com.lilamaris.cozyr.reservation.jpa.support.DailyUsageTestSupport.assertDailyReservationUsageThat;
import static com.lilamaris.cozyr.reservation.jpa.support.ReservationTestConfiguration.NOW;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.jpa.show-sql=false",
        "spring.flyway.enabled=true",
        "cozyr.reservation-service.application.timezone=UTC",
        "spring.datasource.hikari.connection-init-sql=SET lock_timeout = '10s'"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ReservationTestConfiguration.class, RoomOpPolicyTOCTOUTest.TestConfiguration.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("RoomOpPolicy Time-of-check to Time-of-use 테스트")
public class RoomOpPolicyTOCTOUTest {
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18.3-alpine");
    private UserTestSupport.TestContext userContext;
    private RoomTestSupport.TestContext roomContext;

    @Autowired
    private JdbcClient jdbcClient;

    @DynamicPropertySource
    static void database(DynamicPropertyRegistry registry) {
        POSTGRES.start();
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeEach
    void setup() {
        userContext = UserTestSupport.createContext(jdbcClient, NOW);
        roomContext = RoomTestSupport.createContext(jdbcClient, userContext.firstUserId(), NOW);
    }

    @AfterEach
    void cleanup() {
        if (roomContext != null) RoomTestSupport.cleanup(jdbcClient, roomContext);
        if (userContext != null) UserTestSupport.cleanup(jdbcClient, userContext);
    }

    @AfterAll
    void stopContainer() {
        POSTGRES.stop();
    }

    @Autowired
    private ControlledRoomOpPolicyStore policyStore;
    @Autowired
    private ReserveSeatUseCase reserveSeatUseCase;
    @Autowired
    private DeactivateRoomUseCase deactivateRoomUseCase;

    private static final LocalDate DATE = LocalDate.of(2026, 9, 13);
    // The fixture slot is 08:00–09:00 UTC; closing at 08:30 conflicts with it.
    private static final Instant DEACTIVATED_AT = Instant.parse("2026-09-13T08:30:00Z");

    @Test
    @DisplayName("예약이 정책 잠금을 선점하면 예약 커밋 후 이른 비활성화가 거절된다")
    void eval_room_op_status_after_reservation() throws Exception {
        var outcomes = compete(ControlledRoomOpPolicyStore.LockOrder.SHARE_FIRST);

        assertThat(outcomes.reservation().failure()).isNull();
        var reserved = outcomes.reservation().result();
        assertThat(reserved).isNotNull();
        assertThat(outcomes.deactivation().failure()).isNotNull();
        assertThat(outcomes.deactivation().failure().getApplicationCode())
                .isEqualTo(ReservationServiceProgressCode.ROOM_DEACTIVATION_TOO_EARLY);

        assertReservationByRoomThat(jdbcClient, roomContext.roomId())
                .extracting(ReservationAssertion.ReservationAssertRow::id)
                .containsExactly(reserved.reservationId());
        assertActiveOccupanciesThat(jdbcClient, roomContext.targetSeatLocator(), DATE)
                .extracting(SeatOccupancyAssertion.SeatOccupancyAssertRow::reservationId)
                .containsExactly(reserved.reservationId());
        assertDailyReservationUsageThat(jdbcClient, userContext.firstUserId(), roomContext.roomId(), DATE)
                .hasCount(1);
        assertPolicy(NOW, null);
    }

    @Test
    @DisplayName("비활성화가 정책 잠금을 선점하면 변경된 운영 시간을 벗어난 예약이 거절된다")
    void eval_reservation_after_update_room_op_policy() throws Exception {
        var outcomes = compete(ControlledRoomOpPolicyStore.LockOrder.UPDATE_FIRST);

        assertThat(outcomes.deactivation().failure()).isNull();
        assertThat(outcomes.reservation().result()).isNull();
        assertThat(outcomes.reservation().failure()).isNotNull();
        assertThat(outcomes.reservation().failure().getApplicationCode())
                .isEqualTo(ReservationServiceProgressCode.RESERVATION_OUTSIDE_OPERATING_HOURS);

        assertReservationByRoomThat(jdbcClient, roomContext.roomId()).isEmpty();
        assertActiveOccupanciesThat(jdbcClient, roomContext.targetSeatLocator(), DATE).isEmpty();
        assertDailyReservationUsageThat(jdbcClient, roomContext.roomId(), DATE).isEmpty();
        assertPolicy(null, DEACTIVATED_AT);
    }

    private Outcomes compete(ControlledRoomOpPolicyStore.LockOrder order) throws Exception {
        policyStore.prepare(order);
        var reservation = ReserveSeatCommand.of(userContext.firstUserId(), roomContext.targetSeatLocator(),
                DATE, Set.of(roomContext.slotId()));
        var deactivation = DeactivateRoomCommand.of(roomContext.targetSeatLocator().roomId(),
                DEACTIVATED_AT, userContext.firstUserId());
        var executor = Executors.newFixedThreadPool(2);
        try {
            var reservationFuture = executor.submit(() -> capture(() -> reserveSeatUseCase.reserve(reservation)));
            var deactivationFuture = executor.submit(() -> capture(() -> {
                deactivateRoomUseCase.deactivate(deactivation);
                return null;
            }));
            return new Outcomes(reservationFuture.get(30, TimeUnit.SECONDS),
                    deactivationFuture.get(30, TimeUnit.SECONDS));
        } finally {
            executor.shutdownNow();
            assertThat(executor.awaitTermination(15, TimeUnit.SECONDS))
                    .as("policy and reservation workers must terminate before cleanup").isTrue();
        }
    }

    private Outcome capture(Callable<ReserveSeatResult> action) throws Exception {
        try {
            return new Outcome(action.call(), null);
        } catch (ApplicationException e) {
            return new Outcome(null, e);
        }
    }

    private void assertPolicy(Instant activatedAt, Instant deactivatedAt) {
        jdbcClient.sql("SELECT activated_at, deactivated_at FROM room_op_policy WHERE room_id = :roomId")
                .param("roomId", roomContext.roomId())
                .query((rs, rowNum) -> {
                    var activated = rs.getTimestamp("activated_at");
                    var deactivated = rs.getTimestamp("deactivated_at");
                    assertThat(activated == null ? null : activated.toInstant()).isEqualTo(activatedAt);
                    assertThat(deactivated == null ? null : deactivated.toInstant()).isEqualTo(deactivatedAt);
                    return true;
                }).single();
    }

    private record Outcome(ReserveSeatResult result, ApplicationException failure) {}
    private record Outcomes(Outcome reservation, Outcome deactivation) {}

    @Configuration(proxyBeanMethods = false)
    @Import({UpdateRoomService.class, RoomInternalUpdateService.class, RoomJdbcAdapter.class,
            RoomJpaAdapter.class, ReservableScheduleReaderJdbcAdapter.class})
    static class TestConfiguration {
        @Primary
        @Bean
        ControlledRoomOpPolicyStore controlledRoomOpPolicyStore(RoomOpPolicyRepository repository) {
            return new ControlledRoomOpPolicyStore(new RoomOpPolicyJpaAdapter(repository));
        }
    }
}
