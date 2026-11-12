package com.example.kursweather.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.kursweather.WeatherApplication
import com.example.kursweather.ui.cities.CitiesViewModel
import com.example.kursweather.ui.forecast.ForecastViewModel
import com.example.kursweather.ui.navigation.WeatherRoute
import com.example.kursweather.ui.search.SearchViewModel
import com.example.kursweather.ui.settings.SettingsViewModel

/**
 * Фабрика всех ViewModel приложения: зависимости берутся из контейнера WeatherApplication.
 */
object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            val container = weatherApplication().container
            CitiesViewModel(
                container.cityRepository,
                container.weatherRepository,
                container.settingsRepository
            )
        }
        initializer {
            val container = weatherApplication().container
            SearchViewModel(container.cityRepository, container.isCityApiConfigured)
        }
        initializer {
            val container = weatherApplication().container
            ForecastViewModel(
                cityId = createSavedStateHandle().toRoute<WeatherRoute.Forecast>().cityId,
                cityRepository = container.cityRepository,
                weatherRepository = container.weatherRepository,
                settingsRepository = container.settingsRepository
            )
        }
        initializer {
            SettingsViewModel(weatherApplication().container.settingsRepository)
        }
    }
}

fun CreationExtras.weatherApplication(): WeatherApplication =
    this[APPLICATION_KEY] as WeatherApplication
