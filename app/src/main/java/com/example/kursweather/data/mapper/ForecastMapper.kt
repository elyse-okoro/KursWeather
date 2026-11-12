package com.example.kursweather.data.mapper

import com.example.kursweather.data.remote.dto.CurrentDto
import com.example.kursweather.data.remote.dto.DailyDto
import com.example.kursweather.data.remote.dto.ForecastResponseDto
import com.example.kursweather.data.remote.dto.HourlyDto
import com.example.kursweather.domain.model.CurrentWeather
import com.example.kursweather.domain.model.DailyForecast
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.domain.model.HourlyForecast
import com.example.kursweather.domain.model.WeatherCondition
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary
import java.time.LocalDate
import java.time.LocalDateTime

fun ForecastResponseDto.toForecast(settings: WeatherSettings): Forecast = Forecast(
    current = current.toCurrentWeather(),
    hourly = hourly?.toHourlyForecast().orEmpty(),
    daily = daily?.toDailyForecast().orEmpty(),
    settings = settings
)

fun ForecastResponseDto.toSummary(settings: WeatherSettings): WeatherSummary = WeatherSummary(
    current = current.toCurrentWeather(),
    todayMax = daily?.temperatureMax?.firstOrNull(),
    todayMin = daily?.temperatureMin?.firstOrNull(),
    settings = settings
)

fun CurrentDto.toCurrentWeather(): CurrentWeather = CurrentWeather(
    time = LocalDateTime.parse(time),
    temperature = temperature,
    apparentTemperature = apparentTemperature,
    humidity = humidity,
    windSpeed = windSpeed,
    pressureHpa = pressure,
    condition = WeatherCondition.fromWmoCode(weatherCode),
    isDay = isDay == 1
)

/** Часы без температуры (бывает на краю прогноза) пропускаем. */
fun HourlyDto.toHourlyForecast(): List<HourlyForecast> = time.indices.mapNotNull { i ->
    val value = temperature.getOrNull(i) ?: return@mapNotNull null
    HourlyForecast(
        time = LocalDateTime.parse(time[i]),
        temperature = value,
        condition = WeatherCondition.fromWmoCode(weatherCode.getOrNull(i)),
        precipitationProbability = precipitationProbability.getOrNull(i),
        isDay = isDay.getOrNull(i) != 0
    )
}

fun DailyDto.toDailyForecast(): List<DailyForecast> = time.indices.mapNotNull { i ->
    val max = temperatureMax.getOrNull(i) ?: return@mapNotNull null
    val min = temperatureMin.getOrNull(i) ?: return@mapNotNull null
    DailyForecast(
        date = LocalDate.parse(time[i]),
        condition = WeatherCondition.fromWmoCode(weatherCode.getOrNull(i)),
        temperatureMax = max,
        temperatureMin = min,
        precipitationProbability = precipitationProbability.getOrNull(i),
        sunrise = sunrise.getOrNull(i)?.let { LocalDateTime.parse(it).toLocalTime() },
        sunset = sunset.getOrNull(i)?.let { LocalDateTime.parse(it).toLocalTime() }
    )
}
