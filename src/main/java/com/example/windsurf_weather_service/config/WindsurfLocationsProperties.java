package com.example.windsurf_weather_service.config;

import com.example.windsurf_weather_service.model.WindsurfLocation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "windsurf")
public class WindsurfLocationsProperties {

    private List<WindsurfLocation> locations = new ArrayList<>();
}
