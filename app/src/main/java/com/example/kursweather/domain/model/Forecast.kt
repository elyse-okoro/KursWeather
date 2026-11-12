package com.example.kursweather.domain.model

/** Полный прогноз для экрана города: сейчас, ближайшие часы и дни. */
data class Forecast(
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val settings: WeatherSettings
)
