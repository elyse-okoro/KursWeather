package com.example.kursweather.data.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WindSpeedUnit
import com.example.kursweather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Единицы измерения хранятся в Preferences DataStore по именам значений enum.
 */
class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val settings: Flow<WeatherSettings> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                Log.e(TAG, "Failed to read settings", error)
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences ->
            val defaults = WeatherSettings()
            WeatherSettings(
                temperatureUnit = preferences[TEMPERATURE_UNIT]
                    ?.let { name -> TemperatureUnit.entries.firstOrNull { it.name == name } }
                    ?: defaults.temperatureUnit,
                windSpeedUnit = preferences[WIND_SPEED_UNIT]
                    ?.let { name -> WindSpeedUnit.entries.firstOrNull { it.name == name } }
                    ?: defaults.windSpeedUnit
            )
        }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        dataStore.edit { it[TEMPERATURE_UNIT] = unit.name }
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) {
        dataStore.edit { it[WIND_SPEED_UNIT] = unit.name }
    }

    private companion object {
        const val TAG = "SettingsRepository"
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val WIND_SPEED_UNIT = stringPreferencesKey("wind_speed_unit")
    }
}
