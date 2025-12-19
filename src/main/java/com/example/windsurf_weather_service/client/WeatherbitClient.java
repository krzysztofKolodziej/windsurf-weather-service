package com.example.windsurf_weather_service.client;

public interface WeatherbitClient {

    WeatherbitDailyForecastResponse getDailyForecast(double lat, double lon);
}
