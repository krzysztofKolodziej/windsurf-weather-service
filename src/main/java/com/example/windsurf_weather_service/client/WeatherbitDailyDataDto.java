package com.example.windsurf_weather_service.client;

public record WeatherbitDailyDataDto(
        String validDate,
        double temp,
        double windSpeed
) {}