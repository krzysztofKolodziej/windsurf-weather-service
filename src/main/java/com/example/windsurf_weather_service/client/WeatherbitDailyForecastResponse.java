package com.example.windsurf_weather_service.client;

import java.util.List;

public record WeatherbitDailyForecastResponse(
        String cityName,
        String countryCode,
        List<WeatherbitDailyDataDto> data
) { }