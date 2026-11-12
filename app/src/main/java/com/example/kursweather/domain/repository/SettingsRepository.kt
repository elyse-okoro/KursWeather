package com.example.kursweather.domain.repository

import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settings: Flow<WeatherSettings>

    suspend fun setTemperatureUnit(unit: TemperatureUnit)

    suspend fun setWindSpeedUnit(unit: WindSpeedUnit)
}
