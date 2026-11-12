package com.example.kursweather.ui.cities

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.WeatherSettings

data class CitiesUiState(
    val cities: List<CityWeatherItem> = emptyList(),
    val isLoaded: Boolean = false,
    val isRefreshing: Boolean = false,
    val settings: WeatherSettings = WeatherSettings(),
    /** Только что удалённый город — для сообщения с кнопкой «Отменить». */
    val removedCity: City? = null
)
