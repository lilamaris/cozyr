package com.lilamaris.cozyr.statistics.jpa.row;

import com.lilamaris.cozyr.statistics.application.model.point.DailyPoint;

import java.time.LocalDate;

public record DailyPointRow(
        LocalDate date,
        long count
) {
    public DailyPoint toPoint() {
        return DailyPoint.of(date, count);
    }
}
