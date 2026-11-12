package com.example.kursweather.ui.cities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary
import com.example.kursweather.domain.repository.CityRepository
import com.example.kursweather.domain.repository.SettingsRepository
import com.example.kursweather.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Список городов из Room и погода для каждого. Погода перезагружается, когда меняется список,
 * единицы измерения или пользователь тянет список вниз.
 */
class CitiesViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val refreshRequests = MutableStateFlow(0)
    private val _uiState = MutableStateFlow(CitiesUiState())
    val uiState: StateFlow<CitiesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                cityRepository.observeCities(),
                settingsRepository.settings,
                refreshRequests
            ) { cities, settings, _ -> cities to settings }
                .collectLatest { (cities, settings) -> loadWeather(cities, settings) }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        refreshRequests.update { it + 1 }
    }

    fun removeCity(city: City) {
        viewModelScope.launch {
            cityRepository.removeCity(city.id)
            _uiState.update { it.copy(removedCity = city) }
        }
    }

    fun undoRemove() {
        val city = _uiState.value.removedCity ?: return
        _uiState.update { it.copy(removedCity = null) }
        viewModelScope.launch { cityRepository.restoreCity(city) }
    }

    fun onRemovedMessageShown() {
        _uiState.update { it.copy(removedCity = null) }
    }

    private suspend fun loadWeather(cities: List<City>, settings: WeatherSettings) {
        val known = _uiState.value.cities.associateBy { it.city.id }
        _uiState.update { state ->
            state.copy(
                isLoaded = true,
                settings = settings,
                cities = cities.map { city ->
                    known[city.id]?.copy(city = city, isLoading = true) ?: CityWeatherItem(city)
                }
            )
        }
        // города грузятся параллельно, каждая карточка обновляется, как только пришёл её ответ
        coroutineScope {
            cities.forEach { city ->
                launch {
                    val summary = loadSummary(city, settings)
                    _uiState.update { state ->
                        state.copy(cities = state.cities.map { item ->
                            if (item.city.id != city.id) item
                            else item.copy(
                                summary = summary ?: item.summary,
                                isLoading = false,
                                hasError = summary == null
                            )
                        })
                    }
                }
            }
        }
        _uiState.update { it.copy(isRefreshing = false) }
    }

    private suspend fun loadSummary(city: City, settings: WeatherSettings): WeatherSummary? =
        try {
            weatherRepository.getSummary(city, settings)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Weather for ${city.name} failed", e)
            null
        }

    private companion object {
        const val TAG = "CitiesViewModel"
    }
}
