package com.example.windsurf_weather_service.web.dto;

import java.time.LocalDate;

public record BestLocationResponse(
        String locationName,
        LocalDate date,
        double averageTemperatureC,
        double windSpeedMs,
        double score
) {
}
