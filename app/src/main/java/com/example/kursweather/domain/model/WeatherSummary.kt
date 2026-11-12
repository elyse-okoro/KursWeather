package com.example.kursweather.domain.model

/** Коротко о погоде для карточки в списке городов. */
data class WeatherSummary(
    val current: CurrentWeather,
    val todayMax: Double?,
    val todayMin: Double?
)
