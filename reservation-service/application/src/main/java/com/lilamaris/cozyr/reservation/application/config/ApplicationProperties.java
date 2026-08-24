package com.lilamaris.cozyr.reservation.application.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.time.ZoneId;

@Validated
@ConfigurationProperties(prefix = "cozyr.reservation-service.application")
public record ApplicationProperties(
        @DefaultValue("UTC")
        @NotNull
        ZoneId timezone,

        @Valid
        @DefaultValue
        @NotNull
        RoomProperties room
) {
    public record RoomProperties(
            @DefaultValue("06:00")
            @NotNull
            LocalTime openTime,

            @DefaultValue("22:00")
            @NotNull
            LocalTime closeTime,

            @DefaultValue("60")
            @Positive
            int slotMinute
    ) {
    }
}