package com.example.windsurf_weather_service.model;

import java.time.LocalDate;

public record DailyForecast(
        LocalDate date,
        double averageTempC,
        double windSpeedMs
) {}
