package com.example.windsurf_weather_service.domain;

import com.example.windsurf_weather_service.model.DailyForecast;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class WindsurfRulesTest {

    @Test
    void shouldBeSuitable_whenWindAndTempAreWithinRanges_inclusiveBounds() {
        var f1 = new DailyForecast(LocalDate.parse("2026-01-01"), 5.0, 5.0);
        assertThat(WindsurfRules.isSuitable(f1)).isTrue();

        var f2 = new DailyForecast(LocalDate.parse("2026-01-02"), 35.0, 18.0);
        assertThat(WindsurfRules.isSuitable(f2)).isTrue();

        var f3 = new DailyForecast(LocalDate.parse("2026-01-03"), 20.0, 10.0);
        assertThat(WindsurfRules.isSuitable(f3)).isTrue();
    }

    @Test
    void shouldNotBeSuitable_whenWindOutOfRange() {
        var tooLow = new DailyForecast(LocalDate.parse("2026-01-01"), 20.0, 4.9);
        var tooHigh = new DailyForecast(LocalDate.parse("2026-01-01"), 20.0, 18.1);

        assertThat(WindsurfRules.isSuitable(tooLow)).isFalse();
        assertThat(WindsurfRules.isSuitable(tooHigh)).isFalse();
    }

    @Test
    void shouldNotBeSuitable_whenTempOutOfRange() {
        var tooCold = new DailyForecast(LocalDate.parse("2026-01-01"), 4.9, 10.0);
        var tooHot  = new DailyForecast(LocalDate.parse("2026-01-01"), 35.1, 10.0);

        assertThat(WindsurfRules.isSuitable(tooCold)).isFalse();
        assertThat(WindsurfRules.isSuitable(tooHot)).isFalse();
    }

    @Test
    void scoreShouldUseFormula_vTimes3PlusTemp() {
        var forecast = new DailyForecast(LocalDate.parse("2026-01-01"), 10.0, 6.0);

        assertThat(WindsurfRules.score(forecast)).isEqualTo(28.0);
    }

}