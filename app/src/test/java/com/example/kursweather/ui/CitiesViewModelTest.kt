package com.example.kursweather.ui

import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.ui.cities.CitiesViewModel
import com.example.kursweather.ui.fake.FakeCityRepository
import com.example.kursweather.ui.fake.FakeSettingsRepository
import com.example.kursweather.ui.fake.FakeWeatherRepository
import com.example.kursweather.ui.fake.TestWeather
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class CitiesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cities = FakeCityRepository()
    private val weather = FakeWeatherRepository()
    private val settings = FakeSettingsRepository()

    private fun createViewModel() = CitiesViewModel(cities, weather, settings)

    @Test
    fun emptyList_isLoadedWithoutRequests() = runTest {
        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.isLoaded)
        assertTrue(viewModel.uiState.value.cities.isEmpty())
        assertEquals(0, weather.requests)
    }

    @Test
    fun addedCities_getTheirWeather() = runTest {
        cities.addCity(TestWeather.izhevsk)
        cities.addCity(TestWeather.kazan)

        val items = createViewModel().uiState.value.cities

        assertEquals(listOf("Izhevsk", "Kazan"), items.map { it.city.name })
        assertTrue(items.all { it.summary != null && !it.isLoading && !it.hasError })
    }

    @Test
    fun weatherError_marksCardButKeepsCity() = runTest {
        cities.addCity(TestWeather.izhevsk)
        weather.error = IOException("offline")

        val item = createViewModel().uiState.value.cities.single()

        assertTrue(item.hasError)
        assertNull(item.summary)
        assertFalse(item.isLoading)
    }

    @Test
    fun changingUnits_reloadsWeatherWithNewSettings() = runTest {
        cities.addCity(TestWeather.izhevsk)
        val viewModel = createViewModel()

        settings.setTemperatureUnit(TemperatureUnit.FAHRENHEIT)

        assertEquals(TemperatureUnit.FAHRENHEIT, weather.lastSettings?.temperatureUnit)
        assertEquals(TemperatureUnit.FAHRENHEIT, viewModel.uiState.value.settings.temperatureUnit)
    }

    @Test
    fun removeAndUndo_returnsCity() = runTest {
        cities.addCity(TestWeather.izhevsk)
        val viewModel = createViewModel()
        val izhevsk = viewModel.uiState.value.cities.single().city

        viewModel.removeCity(izhevsk)
        assertTrue(cities.savedCities.isEmpty())
        assertNotNull(viewModel.uiState.value.removedCity)

        viewModel.undoRemove()
        assertEquals(listOf(izhevsk), cities.savedCities)
        assertNull(viewModel.uiState.value.removedCity)
    }
}
