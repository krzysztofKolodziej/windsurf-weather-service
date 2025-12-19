package com.example.windsurf_weather_service.client;

import com.example.windsurf_weather_service.config.WeatherbitProperties;
import com.example.windsurf_weather_service.exception.WeatherbitClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class WeatherbitHttpClientTest {

    private WeatherbitHttpClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        WeatherbitProperties props = new WeatherbitProperties();
        props.setBaseUrl("http://weatherbit.test");
        props.setApiKey("test-key");

        RestClient.Builder builder = RestClient.builder()
                .baseUrl(props.getBaseUrl());

        this.server = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.build();
        this.client = new WeatherbitHttpClient(restClient, props);    }

    @Test
    void shouldReturnForecast_whenWeatherbitRespondsOk() {
        String json = """
            {
              "city_name": "Jastarnia",
              "country_code": "PL",
              "data": [
                { "valid_date": "2026-01-01", "temp": 10.5, "wind_spd": 7.2 },
                { "valid_date": "2026-01-02", "temp": 11.0, "wind_spd": 6.0 }
              ]
            }
            """;

        server.expect(once(), requestTo("http://weatherbit.test/forecast/daily?lat=54.6966&lon=18.6786&key=test-key"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        WeatherbitDailyForecastResponse response = client.getDailyForecast(54.6966, 18.6786);

        assertThat(response.cityName()).isEqualTo("Jastarnia");
        assertThat(response.countryCode()).isEqualTo("PL");
        assertThat(response.data()).hasSize(2);
        assertThat(response.data().getFirst().validDate()).isEqualTo("2026-01-01");
        assertThat(response.data().getFirst().temp()).isEqualTo(10.5);
        assertThat(response.data().getFirst().windSpeed()).isEqualTo(7.2);

        server.verify();
    }

    @Test
    void shouldThrowWeatherbitClientException_onTimeoutOrNetworkError() {
        server.expect(once(), requestTo("http://weatherbit.test/forecast/daily?lat=1.0&lon=2.0&key=test-key"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withException(new SocketTimeoutException("timeout")));

        assertThatThrownBy(() -> client.getDailyForecast(1.0, 2.0))
                .isInstanceOf(WeatherbitClientException.class)
                .hasMessageContaining("timeout/network");

        server.verify();
    }
}