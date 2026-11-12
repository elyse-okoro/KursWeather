package com.example.kursweather.ui.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursweather.R
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.repository.CityRepository
import com.example.kursweather.domain.repository.SettingsRepository
import com.example.kursweather.domain.repository.WeatherRepository
import com.example.kursweather.ui.common.weatherErrorRes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Прогноз для одного города. id города приходит из аргумента навигации,
 * прогноз перезагружается при смене единиц измерения и по кнопке «Повторить».
 */
class ForecastViewModel(
    private val cityId: Long,
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val reloadRequests = MutableStateFlow(0)
    private val _uiState = MutableStateFlow(ForecastUiState())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(settingsRepository.settings, reloadRequests) { settings, _ -> settings }
                .collectLatest { settings -> load(settings) }
        }
    }

    fun retry() {
        reloadRequests.update { it + 1 }
    }

    private suspend fun load(settings: WeatherSettings) {
        _uiState.update { it.copy(isLoading = true, errorRes = null) }
        try {
            val city = _uiState.value.city ?: cityRepository.getCity(cityId)
            if (city == null) {
                _uiState.update { it.copy(isLoading = false, errorRes = R.string.error_city_not_found) }
                return
            }
            _uiState.update { it.copy(city = city) }
            val forecast = weatherRepository.getForecast(city, settings)
            _uiState.update { it.copy(forecast = forecast, isLoading = false) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Forecast for city $cityId failed", e)
            _uiState.update { it.copy(isLoading = false, errorRes = e.weatherErrorRes()) }
        }
    }

    private companion object {
        const val TAG = "ForecastViewModel"
    }
}
