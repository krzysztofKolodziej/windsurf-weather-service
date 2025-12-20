package com.example.windsurf_weather_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch() {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                "Invalid 'day' parameter. Expected format: YYYY-MM-DD"
        ));
    }

    @ExceptionHandler(WeatherbitClientException.class)
    public ResponseEntity<ApiErrorResponse> handleWeatherbit() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApiErrorResponse("Weather data provider error"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("Unexpected server error"));
    }
}
