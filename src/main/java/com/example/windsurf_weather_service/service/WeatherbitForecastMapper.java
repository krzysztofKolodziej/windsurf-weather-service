package com.example.windsurf_weather_service.service;

import com.example.windsurf_weather_service.client.WeatherbitDailyDataDto;
import com.example.windsurf_weather_service.model.DailyForecast;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherbitForecastMapper {

    @Mapping(target = "date", source = "validDate")
    @Mapping(target = "averageTempC", source = "temp")
    @Mapping(target = "windSpeedMs", source = "windSpeed")
    DailyForecast toDailyForecast(WeatherbitDailyDataDto dto);
}
