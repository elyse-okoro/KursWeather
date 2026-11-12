package com.example.kursweather.domain.model

import java.time.LocalDateTime

data class CurrentWeather(
    val time: LocalDateTime,
    val temperature: Double,
    val apparentTemperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressureHpa: Double,
    val condition: WeatherCondition,
    val isDay: Boolean
)
