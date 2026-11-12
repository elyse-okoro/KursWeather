package com.example.kursweather.ui

import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WindSpeedUnit
import com.example.kursweather.ui.fake.FakeSettingsRepository
import com.example.kursweather.ui.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSettingsRepository()

    @Test
    fun defaultUnits_areCelsiusAndMetersPerSecond() = runTest {
        val viewModel = SettingsViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.settings.collect {} }

        assertEquals(WeatherSettings(), viewModel.settings.value)
    }

    @Test
    fun chosenUnits_areSavedAndShown() = runTest {
        val viewModel = SettingsViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.settings.collect {} }

        viewModel.setTemperatureUnit(TemperatureUnit.FAHRENHEIT)
        viewModel.setWindSpeedUnit(WindSpeedUnit.KILOMETERS_PER_HOUR)

        assertEquals(
            WeatherSettings(TemperatureUnit.FAHRENHEIT, WindSpeedUnit.KILOMETERS_PER_HOUR),
            viewModel.settings.value
        )
    }
}
