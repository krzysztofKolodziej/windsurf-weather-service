package com.example.windsurf_weather_service.model;

public record LocationForecastScore(
        WindsurfLocation location,
        DailyForecast forecast,
        double score
) {}
