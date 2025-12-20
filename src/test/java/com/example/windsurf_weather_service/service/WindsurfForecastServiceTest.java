package com.example.windsurf_weather_service.service;

import com.example.windsurf_weather_service.client.WeatherbitClient;
import com.example.windsurf_weather_service.client.WeatherbitDailyDataDto;
import com.example.windsurf_weather_service.client.WeatherbitDailyForecastResponse;
import com.example.windsurf_weather_service.config.WindsurfLocationsProperties;
import com.example.windsurf_weather_service.model.DailyForecast;
import com.example.windsurf_weather_service.model.WindsurfLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WindsurfForecastServiceTest {

    private final WeatherbitClient weatherbitClient = mock(WeatherbitClient.class);
    private final WeatherbitForecastMapper mapper = mock(WeatherbitForecastMapper.class);

    @BeforeEach
    void setUp() {
        when(mapper.toDailyForecast(any(WeatherbitDailyDataDto.class)))
                .thenAnswer(inv -> {
                    WeatherbitDailyDataDto dto = inv.getArgument(0);
                    return new DailyForecast(
                            LocalDate.parse(dto.validDate()),
                            dto.temp(),
                            dto.windSpeed()
                    );
                });
    }

    @Test
    void shouldReturnEmpty_whenNoLocationHasForecastForGivenDay() {
        WindsurfLocationsProperties props = locations(
                new WindsurfLocation("Jastarnia", "Poland", 1.0, 2.0),
                new WindsurfLocation("Le Morne", "Mauritius", 3.0, 4.0)
        );

        when(weatherbitClient.getDailyForecast(1.0, 2.0))
                .thenReturn(response(day("2026-01-02", 10.0, 7.0)));

        when(weatherbitClient.getDailyForecast(3.0, 4.0))
                .thenReturn(response(day("2026-01-03", 12.0, 8.0)));

        WindsurfForecastService service = new WindsurfForecastService(weatherbitClient, props, mapper);

        var result = service.findBestLocation(LocalDate.parse("2026-01-01"));

        assertThat(result).isEmpty();

        verify(weatherbitClient).getDailyForecast(1.0, 2.0);
        verify(weatherbitClient).getDailyForecast(3.0, 4.0);
        verifyNoMoreInteractions(weatherbitClient);
    }

    @Test
    void shouldPickBestLocation_forGivenDay_usingDomainRules() {
        WindsurfLocationsProperties props = locations(
                new WindsurfLocation("Jastarnia", "Poland", 54.0, 18.0),
                new WindsurfLocation("Le Morne", "Mauritius", -20.0, 57.0),
                new WindsurfLocation("Pissouri", "Cyprus", 34.0, 32.0)
        );

        when(weatherbitClient.getDailyForecast(54.0, 18.0))
                .thenReturn(response(day("2026-01-01", 10.0, 7.0)));

        when(weatherbitClient.getDailyForecast(-20.0, 57.0))
                .thenReturn(response(day("2026-01-01", 12.0, 10.0)));

        when(weatherbitClient.getDailyForecast(34.0, 32.0))
                .thenReturn(response(day("2026-01-01", 30.0, 4.0)));

        WindsurfForecastService service = new WindsurfForecastService(weatherbitClient, props, mapper);

        var result = service.findBestLocation(LocalDate.parse("2026-01-01"));

        assertThat(result).isPresent();
        assertThat(result.get().location().getName()).isEqualTo("Le Morne");
        assertThat(result.get().score()).isEqualTo(42.0);
    }

    @Test
    void shouldReturnEmpty_whenAllCandidatesUnsuitable() {
        WindsurfLocationsProperties props = locations(
                new WindsurfLocation("A", "X", 1.0, 1.0),
                new WindsurfLocation("B", "X", 2.0, 2.0)
        );

        when(weatherbitClient.getDailyForecast(1.0, 1.0))
                .thenReturn(response(day("2026-01-01", 20.0, 4.0)));

        when(weatherbitClient.getDailyForecast(2.0, 2.0))
                .thenReturn(response(day("2026-01-01", 36.0, 10.0)));

        WindsurfForecastService service = new WindsurfForecastService(weatherbitClient, props, mapper);

        var result = service.findBestLocation(LocalDate.parse("2026-01-01"));

        assertThat(result).isEmpty();
    }


    private static WindsurfLocationsProperties locations(WindsurfLocation... locs) {
        WindsurfLocationsProperties p = new WindsurfLocationsProperties();
        p.setLocations(List.of(locs));
        return p;
    }

    private static WeatherbitDailyForecastResponse response(WeatherbitDailyDataDto... days) {
        return new WeatherbitDailyForecastResponse(
                "city",
                "CC",
                List.of(days)
        );
    }

    private static WeatherbitDailyDataDto day(String date, double temp, double wind) {
        return new WeatherbitDailyDataDto(date, temp, wind);
    }

}