package com.example.windsurf_weather_service.service;

import com.example.windsurf_weather_service.client.WeatherbitDailyDataDto;
import com.example.windsurf_weather_service.model.DailyForecast;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface WeatherbitForecastMapper {

    @Mapping(target = "date", source = "validDate", qualifiedByName = "toLocalDate")
    @Mapping(target = "averageTempC", source = "temp")
    @Mapping(target = "windSpeedMs", source = "windSpeed")
    DailyForecast toDailyForecast(WeatherbitDailyDataDto dto);

    @Named("toLocalDate")
    static LocalDate toLocalDate(String date) {
        return date == null ? null : LocalDate.parse(date);
    }
}
