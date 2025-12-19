package com.example.windsurf_weather_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "weatherbit")
public class WeatherbitProperties {

    private String baseUrl;
    private String apiKey;
}
