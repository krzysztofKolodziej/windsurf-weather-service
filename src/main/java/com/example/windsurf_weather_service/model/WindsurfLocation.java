package com.example.windsurf_weather_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WindsurfLocation {

    private String name;
    private String country;
    private double latitude;
    private double longitude;
}
