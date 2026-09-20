package com.lilamaris.cozyr.reservation.jpa;

import com.lilamaris.cozyr.reservation.application.port.in.CancelReserveUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CancelReserveCommand;
import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion;
import com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion;
import com.lilamaris.cozyr.reservation.jpa.support.ReservationTestConfiguration;
import com.lilamaris.cozyr.reservation.jpa.support.ReservationTestSupport;
import com.lilamaris.cozyr.reservation.jpa.support.RoomTestSupport;
import com.lilamaris.cozyr.reservation.jpa.support.UserTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Set;

import static com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion.assertReservationByRoomThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.ReservationAssertion.assertReservationThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion.assertActiveOccupanciesThat;
import static com.lilamaris.cozyr.reservation.jpa.assertion.SeatOccupancyAssertion.assertSeatOccupancyThat;
import static com.lilamaris.cozyr.reservation.jpa.support.DailyUsageTestSupport.assertDailyReservationUsageThat;
import static com.lilamaris.cozyr.reservation.jpa.support.ReservationTestConfiguration.NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.jpa.show-sql=false",
        "spring.flyway.enabled=true"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ReservationTestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Reserve Seat 생명 주기 테스트")
class ReserveSeatLifecycleTest {
    // V4 uses uuidv7(), which requires PostgreSQL 18.
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18.3-alpine");
    private static final LocalDate DATE = LocalDate.of(2026, 9, 13);
    private UserTestSupport.TestContext userContext;
    private RoomTestSupport.TestContext roomContext;

    @Autowired
    private ReserveSeatUseCase reserveSeatUseCase;

    @Autowired
    private CancelReserveUseCase cancelReserveUseCase;

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
        roomContext = RoomTestSupport.createContext(jdbcClient, userContext.firstUserId(), NOW);
    }

    @AfterEach
    void cleanupAfter() {
        ReservationTestSupport.cleanup(jdbcClient, Set.of(userContext.firstUserId(), userContext.secondUserId()));
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
    @DisplayName("예약 시 좌석 점유가 생성되고 일일 예약 횟수가 증가한다")
    void reservation_with_occupancy_and_increase_daily_usage() {
        var command = ReserveSeatCommand.of(userContext.firstUserId(), roomContext.targetSeatLocator(), DATE, Set.of(roomContext.slotId()));

        var reserved = reserveSeatUseCase.reserve(command);

        // 해당 방에는 방금 생성한 예약 하나만 있어야함
        assertReservationByRoomThat(jdbcClient, roomContext.roomId())
                .extracting(ReservationAssertion.ReservationAssertRow::id)
                .containsExactly(reserved.reservationId());

        // firstUserId가 예약을 생성해야함
        assertThat(reserved.reservationUserId()).isEqualTo(userContext.firstUserId());

        // targetSeatLocator에 대해 DATE에 생성된 firstUserId를 위한 예약이 생성되야함
        assertReservationThat(jdbcClient, reserved.reservationId())
                .hasStatus(ReservationStatus.RESERVED)
                .hasSeatLocator(roomContext.targetSeatLocator())
                .hasOccupancyDate(DATE)
                .hasReservedUserId(reserved.reservationUserId());

        // 생성된 예약에 대해서 좌석 점유가 유효해야함
        assertSeatOccupancyThat(jdbcClient, reserved.reservationId())
                .extracting(
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::roomId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::seatId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::occupancyDate,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::slotId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::releasedAt
                )
                .containsExactly(tuple(roomContext.roomId(), roomContext.targetSeatLocator().seatId().getValue(),
                        DATE, roomContext.slotId(), null));

        // 예약한 사용자의 해당 방·날짜에 대한 일일 예약 횟수가 1이어야함
        assertDailyReservationUsageThat(jdbcClient, userContext.firstUserId(), roomContext.roomId(), DATE)
                .hasCount(1);
    }

    @Test
    @DisplayName("예약 취소 시 점유와 사용 횟수가 해제되고 같은 좌석·날짜·시간을 다시 예약할 수 있다")
    void canceledReservationReleasesOccupancyAndUsageForRebooking() {
        var command = ReserveSeatCommand.of(userContext.firstUserId(), roomContext.targetSeatLocator(), DATE,
                Set.of(roomContext.slotId()));

        // 예약: 상태, 점유, 일일 사용 횟수가 함께 저장된다.
        var reserved = reserveSeatUseCase.reserve(command);

        assertReservationThat(jdbcClient, reserved.reservationId())
                .hasStatus(ReservationStatus.RESERVED)
                .hasSeatLocator(command.seatLocator())
                .hasOccupancyDate(DATE)
                .hasReservedUserId(command.reserveUserId());
        assertActiveOccupanciesThat(jdbcClient, command.seatLocator(), DATE)
                .extracting(SeatOccupancyAssertion.SeatOccupancyAssertRow::reservationId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::slotId)
                .containsExactly(tuple(reserved.reservationId(), roomContext.slotId()));
        assertDailyReservationUsageThat(jdbcClient, command.reserveUserId(), roomContext.roomId(), DATE)
                .hasCount(1);

        // 취소: 예약과 점유 이력은 남고, 활성 점유와 사용 횟수는 해제된다.
        cancelReserveUseCase.cancel(CancelReserveCommand.of(reserved.reservationId()));

        assertReservationThat(jdbcClient, reserved.reservationId())
                .hasStatus(ReservationStatus.CANCELED)
                .hasSeatLocator(command.seatLocator())
                .hasOccupancyDate(DATE)
                .hasReservedUserId(command.reserveUserId());
        assertActiveOccupanciesThat(jdbcClient, command.seatLocator(), DATE).isEmpty();
        assertSeatOccupancyThat(jdbcClient, reserved.reservationId())
                .extracting(SeatOccupancyAssertion.SeatOccupancyAssertRow::slotId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::releasedAt)
                .containsExactly(tuple(roomContext.slotId(), NOW));
        assertDailyReservationUsageThat(jdbcClient, command.reserveUserId(), roomContext.roomId(), DATE)
                .hasCount(0);

        // 재예약: 이전 예약을 되살리지 않고 새 예약만 활성 점유를 갖는다.
        var rebooked = reserveSeatUseCase.reserve(command);

        assertThat(rebooked.reservationId()).isNotEqualTo(reserved.reservationId());
        assertReservationByRoomThat(jdbcClient, roomContext.roomId())
                .extracting(ReservationAssertion.ReservationAssertRow::id,
                        ReservationAssertion.ReservationAssertRow::status)
                .containsExactlyInAnyOrder(
                        tuple(reserved.reservationId(), ReservationStatus.CANCELED),
                        tuple(rebooked.reservationId(), ReservationStatus.RESERVED));
        assertReservationThat(jdbcClient, rebooked.reservationId())
                .hasSeatLocator(command.seatLocator())
                .hasOccupancyDate(DATE)
                .hasReservedUserId(command.reserveUserId());
        assertActiveOccupanciesThat(jdbcClient, command.seatLocator(), DATE)
                .extracting(SeatOccupancyAssertion.SeatOccupancyAssertRow::reservationId,
                        SeatOccupancyAssertion.SeatOccupancyAssertRow::slotId)
                .containsExactly(tuple(rebooked.reservationId(), roomContext.slotId()));
        assertDailyReservationUsageThat(jdbcClient, command.reserveUserId(), roomContext.roomId(), DATE)
                .hasCount(1);
    }
}
