package com.example.kursweather.ui.cities

import com.example.kursweather.domain.model.City

data class CitiesUiState(
    val cities: List<CityWeatherItem> = emptyList(),
    val isLoaded: Boolean = false,
    val isRefreshing: Boolean = false,
    /** Только что удалённый город — для сообщения с кнопкой «Отменить». */
    val removedCity: City? = null
)
