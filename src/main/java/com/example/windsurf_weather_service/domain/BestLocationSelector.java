package com.example.windsurf_weather_service.domain;

import com.example.windsurf_weather_service.model.DailyForecast;
import com.example.windsurf_weather_service.model.LocationForecastScore;
import com.example.windsurf_weather_service.model.WindsurfLocation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BestLocationSelector {

    public Optional<LocationForecastScore> selectBest(List<Candidate> candidates) {
        return candidates.stream()
                .map(c -> toScoreIfSuitable(c.location, c.forecast))
                .flatMap(Optional::stream)
                .max(Comparator.comparingDouble(LocationForecastScore::score));
    }

    private Optional<LocationForecastScore> toScoreIfSuitable(WindsurfLocation location, DailyForecast forecast) {
        if (!WindsurfRules.isSuitable(forecast)) {
            return Optional.empty();
        }
        double score = WindsurfRules.score(forecast);
        return Optional.of(new LocationForecastScore(location, forecast, score));
    }

    public record Candidate(WindsurfLocation location, DailyForecast forecast) {}
}
