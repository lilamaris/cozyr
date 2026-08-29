package com.lilamaris.cozyr.reservation.application.model.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LocalTimeSchedule 유닛 테스트")
public class LocalTimeScheduleTest {

    @Test
    @DisplayName("LocalTime 타입 from, to를 전달해서 생성한다")
    void create() {
        var from = LocalTime.of(6, 0);
        var to = LocalTime.of(8, 0);

        var schedule = LocalTimeSchedule.of(from, to);

        assertThat(schedule).isInstanceOf(LocalTimeSchedule.class);

        assertThat(schedule.from()).isEqualTo(from);
        assertThat(schedule.to()).isEqualTo(to);
    }

    @Test
    @DisplayName("to가 from보다 이전이면 자정을 넘었음을 확인할 수 있다")
    void isOverMidnight_is_true_when_to_is_before_than_from() {
        var from = LocalTime.of(8, 0);
        var to = LocalTime.of(6, 0);

        var schedule = LocalTimeSchedule.of(from, to);

        assertThat(schedule.isOverMidnight()).isTrue();
    }

    @Test
    @DisplayName("from과 to 간의 시간차를 Duration 형식으로 반환한다")
    void return_range_of_diff() {
        var from = LocalTime.of(6, 0);
        var to = LocalTime.of(8, 0);

        var singleDay = LocalTimeSchedule.of(from, to);
        assertThat(singleDay.range()).isEqualTo(Duration.between(from, to));

        from = LocalTime.of(23, 0);
        to = LocalTime.MIDNIGHT;

        var overMidnight = LocalTimeSchedule.of(from, to);
        assertThat(overMidnight.range()).isEqualTo(Duration.between(from, to).plusDays(1));
    }
}