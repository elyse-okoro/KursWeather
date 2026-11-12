package com.example.kursweather.domain.model

/** Единицы измерения, выбранные пользователем. По умолчанию — как принято в России. */
data class WeatherSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.METERS_PER_SECOND
)
