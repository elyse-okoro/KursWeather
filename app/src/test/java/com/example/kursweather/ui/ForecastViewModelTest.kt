package com.example.kursweather.ui

import com.example.kursweather.R
import com.example.kursweather.domain.model.WindSpeedUnit
import com.example.kursweather.ui.fake.FakeCityRepository
import com.example.kursweather.ui.fake.FakeSettingsRepository
import com.example.kursweather.ui.fake.FakeWeatherRepository
import com.example.kursweather.ui.fake.TestWeather
import com.example.kursweather.ui.forecast.ForecastViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ForecastViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cities = FakeCityRepository()
    private val weather = FakeWeatherRepository()
    private val settings = FakeSettingsRepository()

    private fun createViewModel(cityId: Long) = ForecastViewModel(cityId, cities, weather, settings)

    @Test
    fun forecast_isLoadedForCity() = runTest {
        val id = cities.addCity(TestWeather.izhevsk)

        val state = createViewModel(id).uiState.value

        assertEquals("Izhevsk", state.city?.name)
        assertEquals(24, state.forecast?.hourly?.size)
        assertEquals(16, state.forecast?.daily?.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun missingCity_showsMessage() = runTest {
        val state = createViewModel(cityId = 42).uiState.value

        assertEquals(R.string.error_city_not_found, state.errorRes)
        assertNull(state.forecast)
    }

    @Test
    fun retry_afterNetworkError_loadsForecast() = runTest {
        val id = cities.addCity(TestWeather.izhevsk)
        weather.error = IOException("offline")
        val viewModel = createViewModel(id)
        assertEquals(R.string.error_no_internet, viewModel.uiState.value.errorRes)

        weather.error = null
        viewModel.retry()

        assertNotNull(viewModel.uiState.value.forecast)
        assertNull(viewModel.uiState.value.errorRes)
    }

    @Test
    fun windUnitChange_reloadsForecast() = runTest {
        val id = cities.addCity(TestWeather.izhevsk)
        val viewModel = createViewModel(id)

        settings.setWindSpeedUnit(WindSpeedUnit.KILOMETERS_PER_HOUR)

        assertEquals(WindSpeedUnit.KILOMETERS_PER_HOUR, viewModel.uiState.value.forecast?.settings?.windSpeedUnit)
    }
}
