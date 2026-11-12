package com.example.kursweather.ui.forecast

import androidx.annotation.StringRes
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.Forecast

data class ForecastUiState(
    val city: City? = null,
    val forecast: Forecast? = null,
    val isLoading: Boolean = true,
    @param:StringRes val errorRes: Int? = null
)
