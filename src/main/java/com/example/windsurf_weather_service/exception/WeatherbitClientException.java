package com.example.windsurf_weather_service.exception;

public class WeatherbitClientException extends RuntimeException {
    public WeatherbitClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
