package com.example.kursweather.ui.fake

import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WindSpeedUnit
import com.example.kursweather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val state = MutableStateFlow(WeatherSettings())

    override val settings: Flow<WeatherSettings> = state

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        state.value = state.value.copy(temperatureUnit = unit)
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) {
        state.value = state.value.copy(windSpeedUnit = unit)
    }
}
