package com.example.windsurf_weather_service.controller;

import com.example.windsurf_weather_service.web.error.GlobalExceptionHandler;
import com.example.windsurf_weather_service.web.error.InvalidRequestException;
import com.example.windsurf_weather_service.model.DailyForecast;
import com.example.windsurf_weather_service.model.LocationForecastScore;
import com.example.windsurf_weather_service.model.WindsurfLocation;
import com.example.windsurf_weather_service.web.validation.ForecastDayValidator;
import com.example.windsurf_weather_service.service.WindsurfForecastService;
import com.example.windsurf_weather_service.web.controller.WindsurfForecastController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WindsurfForecastController.class)
@Import(GlobalExceptionHandler.class)
class WindsurfForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WindsurfForecastService service;

    @MockitoBean
    private ForecastDayValidator dayValidator;

    @Test
    void shouldReturn200_andResponseBody_whenBestLocationFound() throws Exception {
        LocalDate day = LocalDate.parse("2026-01-01");

        var location = WindsurfLocation.builder()
                .name("Le Morne")
                .country("Mauritius")
                .latitude(-20.453)
                .longitude(57.314)
                .build();

        var forecast = new DailyForecast(day, 12.0, 10.0);
        var score = 42.0;
        var result = new LocationForecastScore(location, forecast, score);

        when(service.findBestLocation(day)).thenReturn(Optional.of(result));

        mockMvc.perform(get("/api/v1/best-location")
                        .param("day", "2026-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.locationName").value("Le Morne"))
                .andExpect(jsonPath("$.date").value("2026-01-01"))
                .andExpect(jsonPath("$.averageTemperatureC").value(12.0))
                .andExpect(jsonPath("$.windSpeedMs").value(10.0))
                .andExpect(jsonPath("$.score").value(42.0));

        verify(dayValidator).validate(day);
        verify(service).findBestLocation(day);
        verifyNoMoreInteractions(service, dayValidator);
    }

    @Test
    void shouldReturn204_whenNoSuitableLocationFound() throws Exception {
        LocalDate day = LocalDate.parse("2026-01-01");
        when(service.findBestLocation(day)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/best-location")
                        .param("day", "2026-01-01"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dayValidator).validate(day);
        verify(service).findBestLocation(day);
        verifyNoMoreInteractions(service, dayValidator);
    }

    @Test
    void shouldReturn400_whenDayIsInvalidAccordingToValidator() throws Exception {
        LocalDate day = LocalDate.parse("2026-01-01");
        doThrow(new InvalidRequestException("Parameter 'day' must not be in the past"))
                .when(dayValidator).validate(day);

        mockMvc.perform(get("/api/v1/best-location")
                        .param("day", "2026-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Parameter 'day' must not be in the past"));

        verify(dayValidator).validate(day);
        verifyNoInteractions(service);
    }

    @Test
    void shouldReturn400_whenDayHasInvalidFormat() throws Exception {
        mockMvc.perform(get("/api/v1/best-location")
                        .param("day", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid 'day' parameter. Expected format: YYYY-MM-DD"));

        verifyNoInteractions(service, dayValidator);
    }

    @Test
    void shouldReturn400_whenDayParamMissing() throws Exception {
        mockMvc.perform(get("/api/v1/best-location")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service, dayValidator);
    }
}