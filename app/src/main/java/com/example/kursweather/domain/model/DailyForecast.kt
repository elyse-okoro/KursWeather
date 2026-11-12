package com.example.kursweather.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class DailyForecast(
    val date: LocalDate,
    val condition: WeatherCondition,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val precipitationProbability: Int?,
    val sunrise: LocalTime?,
    val sunset: LocalTime?
)
