package com.example.windsurf_weather_service.domain;

import com.example.windsurf_weather_service.model.DailyForecast;
import com.example.windsurf_weather_service.model.WindsurfLocation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BestLocationSelectorTest {

    private final BestLocationSelector selector = new BestLocationSelector();
    private final LocalDate day = LocalDate.parse("2026-01-01");

    @Test
    void shouldReturnEmpty_whenNoLocationIsSuitable() {
        WindsurfLocation loc1 = location("Jastarnia");
        WindsurfLocation loc2 = location("Pissouri");

        var c1 = candidate(loc1, new DailyForecast(day, 20.0, 4.0));
        var c2 = candidate(loc2, new DailyForecast(day, 36.0, 10.0));

        var result = selector.selectBest(List.of(c1, c2));

        assertThat(result).isEmpty();
    }

    @Test
    void shouldPickBestLocationByHighestScore_amongSuitableOnes() {
        var jastarnia = location("Jastarnia");
        var leMorne = location("Le Morne");
        var fortaleza = location("Fortaleza");

        var c1 = candidate(jastarnia, new DailyForecast(day, 10.0, 7.0));

        var c2 = candidate(leMorne, new DailyForecast(day, 12.0, 10.0));

        var c3 = candidate(fortaleza, new DailyForecast(day, 15.0, 8.0));

        var result = selector.selectBest(List.of(c1, c2, c3));

        assertThat(result).isPresent();
        assertThat(result.get().location().getName()).isEqualTo("Le Morne");
        assertThat(result.get().score()).isEqualTo(42.0);
    }

    @Test
    void shouldIgnoreUnsuitableCandidates_evenIfTheyWouldScoreHigh() {
        var good = location("Bridgetown");
        var bad = location("Pissouri");

        var goodCandidate = candidate(good, new DailyForecast(day, 20.0, 6.0));

        var badCandidate = candidate(bad, new DailyForecast(day, 35.0, 19.0));

        var result = selector.selectBest(List.of(goodCandidate, badCandidate));

        assertThat(result).isPresent();
        assertThat(result.get().location().getName()).isEqualTo("Bridgetown");
    }

    private WindsurfLocation location(String name) {
        return new WindsurfLocation(name, "X", 0.0, 0.0);
    }

    private BestLocationSelector.Candidate candidate(WindsurfLocation location, DailyForecast forecast) {
        return new BestLocationSelector.Candidate(location, forecast);
    }
}