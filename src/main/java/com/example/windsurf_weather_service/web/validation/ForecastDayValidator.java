package com.example.windsurf_weather_service.web.validation;

import com.example.windsurf_weather_service.web.error.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class ForecastDayValidator {

    private static final int FORECAST_RANGE_DAYS = 16;

    private final Clock clock;

    public void validate(LocalDate day) {
        LocalDate today = LocalDate.now(clock);

        if (day == null) {
            throw new InvalidRequestException("Parameter 'day' is required");
        }

        if (day.isBefore(today)) {
            throw new InvalidRequestException("Parameter 'day' must not be in the past");
        }

        if (day.isAfter(today.plusDays(FORECAST_RANGE_DAYS))) {
            throw new InvalidRequestException("Parameter 'day' must be within " + FORECAST_RANGE_DAYS + "-day forecast range");
        }
    }
}
