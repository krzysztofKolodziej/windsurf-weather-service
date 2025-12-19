package com.example.windsurf_weather_service.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherbitDailyForecastResponse(
        @JsonProperty("city_name")
        String cityName,
        @JsonProperty("country_code")
        String countryCode,
        @JsonProperty("data")
        List<WeatherbitDailyDataDto> data
) { }