package com.example.kursweather.domain.model

import java.time.LocalDateTime

data class HourlyForecast(
    val time: LocalDateTime,
    val temperature: Double,
    val condition: WeatherCondition,
    val precipitationProbability: Int?,
    val isDay: Boolean
)
