package com.example.windsurf_weather_service.domain;

import com.example.windsurf_weather_service.model.DailyForecast;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WindsurfRules {

    public static final double MIN_WIND_MS = 5.0;
    public static final double MAX_WIND_MS = 18.0;

    public static final double MIN_TEMP_C = 5.0;
    public static final double MAX_TEMP_C = 35.0;

    public static boolean isSuitable(DailyForecast forecast) {
        return isInRangeInclusive(forecast.windSpeedMs(), MIN_WIND_MS, MAX_WIND_MS)
                && isInRangeInclusive(forecast.averageTempC(), MIN_TEMP_C, MAX_TEMP_C);
    }

    public static double score(DailyForecast forecast) {
        return forecast.windSpeedMs() * 3.0 + forecast.averageTempC();
    }

    private static boolean isInRangeInclusive(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
