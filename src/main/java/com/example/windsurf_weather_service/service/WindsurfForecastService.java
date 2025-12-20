package com.example.windsurf_weather_service.service;

import com.example.windsurf_weather_service.client.WeatherbitClient;
import com.example.windsurf_weather_service.config.WindsurfLocationsProperties;
import com.example.windsurf_weather_service.domain.BestLocationSelector;
import com.example.windsurf_weather_service.model.LocationForecastScore;
import com.example.windsurf_weather_service.model.WindsurfLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WindsurfForecastService {

    private final WeatherbitClient weatherbitClient;
    private final WindsurfLocationsProperties locationsProperties;
    private final WeatherbitForecastMapper mapper;
    private final BestLocationSelector selector = new BestLocationSelector();

    public Optional<LocationForecastScore> findBestLocation(LocalDate day) {

        List<BestLocationSelector.Candidate> candidates =
                locationsProperties.getLocations().stream()
                        .map(location -> buildCandidate(location, day))
                        .flatMap(Optional::stream)
                        .toList();

        return selector.selectBest(candidates);
    }

    private Optional<BestLocationSelector.Candidate> buildCandidate(WindsurfLocation location, LocalDate day) {
        var response = weatherbitClient.getDailyForecast(location.getLatitude(), location.getLongitude());

        return response.data().stream()
                .map(mapper::toDailyForecast)
                .filter(Objects::nonNull)
                .filter(forecast -> forecast.date().equals(day))
                .findFirst()
                .map(forecast -> new BestLocationSelector.Candidate(location, forecast));
    }
}


