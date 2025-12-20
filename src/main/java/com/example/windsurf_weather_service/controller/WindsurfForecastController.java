package com.example.windsurf_weather_service.controller;

import com.example.windsurf_weather_service.dto.BestLocationResponse;
import com.example.windsurf_weather_service.service.WindsurfForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("api/v1/")
@RestController
public class WindsurfForecastController {

    private final WindsurfForecastService service;

    @GetMapping("best-location")
    public ResponseEntity<BestLocationResponse> getBestLocation(
            @RequestParam("day")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate day
    ) {
        return service.findBestLocation(day)
                .map(result -> ResponseEntity.ok(
                        new BestLocationResponse(
                                result.location().getName(),
                                result.forecast().date(),
                                result.forecast().averageTempC(),
                                result.forecast().windSpeedMs(),
                                result.score()
                        )
                ))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
