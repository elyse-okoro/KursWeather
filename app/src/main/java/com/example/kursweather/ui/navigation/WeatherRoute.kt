package com.example.kursweather.ui.navigation

import kotlinx.serialization.Serializable

/** Экраны приложения для типобезопасной навигации Navigation Compose. */
sealed interface WeatherRoute {

    @Serializable
    data object Cities : WeatherRoute

    @Serializable
    data object Search : WeatherRoute

    @Serializable
    data class Forecast(val cityId: Long) : WeatherRoute

    @Serializable
    data object Settings : WeatherRoute
}
