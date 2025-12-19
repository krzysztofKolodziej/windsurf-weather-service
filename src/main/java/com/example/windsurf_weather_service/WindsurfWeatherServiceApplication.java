package com.example.windsurf_weather_service;

import com.example.windsurf_weather_service.config.WeatherbitProperties;
import com.example.windsurf_weather_service.config.WindsurfLocationsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WindsurfLocationsProperties.class, WeatherbitProperties.class})
public class WindsurfWeatherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindsurfWeatherServiceApplication.class, args);
	}

}
