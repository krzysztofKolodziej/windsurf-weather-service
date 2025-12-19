package com.example.windsurf_weather_service.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherbitDailyDataDto(
        @JsonProperty("valid_date")
        String validDate,
        @JsonProperty("temp")
        double temp,
        @JsonProperty("wind_spd")
        double windSpeed
) {}