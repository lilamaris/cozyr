package com.lilamaris.cozyr.reservation.application.model.schedule;

import com.lilamaris.cozyr.reservation.contract.model.LocalTimeSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ScheduleFactory 유닛 테스트")
public class ScheduleFactoryTest {
    ScheduleFactory factory;

    @BeforeEach
    void run() {
        factory = new ScheduleFactory();
    }

    @Test
    @DisplayName("startTime부터 endTime까지 step 단위의 시간 구간 배열을 만든다")
    void create_local_time_schedule_list() {
        var startTime = LocalTime.of(9, 0);
        var endTime = LocalTime.of(12, 0);
        var step = Duration.ofHours(1);

        List<LocalTimeSchedule> schedules = factory.create(startTime, endTime, step);

        assertThat(schedules)
                .hasSize(3)
                .extracting("from", "to")
                .containsExactly(
                        tuple(startTime, LocalTime.of(10, 0)),
                        tuple(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                        tuple(LocalTime.of(11, 0), endTime)
                );
    }

    @Test
    @DisplayName("endTime이 startTime보다 이전이면, startTime부터 자정을 넘어 endTime까지 스케쥴을 만든다")
    void create_schedule_over_midnight() {
        var startTime = LocalTime.of(22, 30);
        var endTime = LocalTime.of(1, 30);
        var step = Duration.ofHours(1);

        // 22:30 ~ 23:30 (isOverMidnight: false)
        // 23:30 ~ 0:30 (isOverMidnight: true)
        // 0:30 ~ 1:30 (isOverMidnight: false)

        var schedules = factory.create(startTime, endTime, step);

        assertThat(schedules)
                .hasSize(3)
                .extracting("from", "to", "isOverMidnight")
                .containsExactly(
                        tuple(startTime, LocalTime.of(23, 30), false),
                        tuple(LocalTime.of(23, 30), LocalTime.of(0, 30), true),
                        tuple(LocalTime.of(0, 30), LocalTime.of(1, 30), false)
                );
    }

    @Test
    @DisplayName("startTime과 endTime이 동일하면 24시간 스케쥴을 만든다")
    void create_24_hour_schedule() {
        var startTime = LocalTime.of(8, 0);
        var step = Duration.ofHours(1);

        var schedules = factory.create(startTime, startTime, step);

        assertThat(schedules).hasSize(24);
        assertThat(schedules.getFirst()).extracting("from", "to")
                .containsExactly(startTime, LocalTime.of(9, 0));
        assertThat(schedules.getLast()).extracting("from", "to")
                .containsExactly(LocalTime.of(7, 0), startTime);

        for (var i = 0; i < schedules.size(); i++) {
            assertThat(schedules.get(i).range()).isEqualTo(step);
            if (i + 1 < schedules.size()) {
                assertThat(schedules.get(i).to()).isEqualTo(schedules.get(i + 1).from());
            }
        }

        assertThat(schedules)
                .filteredOn(LocalTimeSchedule::isOverMidnight)
                .hasSize(1)
                .first()
                .extracting("from", "to")
                .containsExactly(LocalTime.of(23, 0), LocalTime.MIDNIGHT);
    }

    @Test
    @DisplayName("startTime, endTime 시간 구간이 step 단위로 나누어 떨어지지 않으면 예외")
    void throw_if_range_not_divided_into_step() {
        var startTime = LocalTime.of(22, 30);
        var endTime = LocalTime.of(1, 0);
        var step = Duration.ofMinutes(31);

        assertThatThrownBy(() -> factory.create(startTime, endTime, step))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be divided into steps.");

        assertThatThrownBy(() -> factory.create(startTime, startTime, step))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be divided into steps.");
    }
}