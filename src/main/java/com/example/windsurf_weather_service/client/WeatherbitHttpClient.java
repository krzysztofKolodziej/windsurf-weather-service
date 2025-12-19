package com.example.windsurf_weather_service.client;

import com.example.windsurf_weather_service.config.WeatherbitProperties;
import com.example.windsurf_weather_service.exception.WeatherbitClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class WeatherbitHttpClient implements WeatherbitClient {

    private final RestClient restClient;
    private final WeatherbitProperties properties;

    @Override
    public WeatherbitDailyForecastResponse getDailyForecast(double lat, double lon) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast/daily")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("key", properties.getApiKey())
                            .build())
                    .retrieve()
                    .body(WeatherbitDailyForecastResponse.class);
        } catch (ResourceAccessException e) {
            throw new WeatherbitClientException("Weatherbit request failed (timeout/network)", e);
        }
    }
}
