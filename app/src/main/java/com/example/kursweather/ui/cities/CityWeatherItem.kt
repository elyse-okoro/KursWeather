package com.example.kursweather.ui.cities

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.WeatherSummary

/** Карточка города в списке: сам город и его погода, пока она грузится или если не загрузилась. */
data class CityWeatherItem(
    val city: City,
    val summary: WeatherSummary? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)
