package com.example.kursweather.data

import com.example.kursweather.data.remote.OpenMeteoService
import com.example.kursweather.data.remote.RetrofitFactory
import com.example.kursweather.data.repository.DefaultWeatherRepository
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WeatherCondition
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WindSpeedUnit
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/** Разбор настоящего ответа Open-Meteo (сохранён в resources) и параметры запроса. */
class WeatherRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var repository: DefaultWeatherRepository
    private val izhevsk = City(id = 1, name = "Izhevsk", country = "RU", latitude = 56.85, longitude = 53.2333)

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val service = RetrofitFactory.create(server.url("/").toString()).create(OpenMeteoService::class.java)
        repository = DefaultWeatherRepository(service)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun enqueueForecast() {
        val body = javaClass.classLoader!!.getResource("forecast_izhevsk.json").readText()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
    }

    @Test
    fun forecast_isParsedIntoCurrentHoursAndDays() = runTest {
        enqueueForecast()

        val forecast = repository.getForecast(izhevsk, WeatherSettings())

        assertEquals(24, forecast.hourly.size)
        assertEquals(16, forecast.daily.size)
        assertEquals(WeatherCondition.OVERCAST, forecast.current.condition)
        assertEquals(89, forecast.current.humidity)
        assertEquals(LocalDate.of(2026, 11, 12), forecast.daily.first().date)
        assertNotNull(forecast.daily.first().sunrise)
        assertEquals(WeatherCondition.SNOW, forecast.daily.first().condition)
    }

    @Test
    fun forecast_requestHasCoordinatesUnitsAndSixteenDays() = runTest {
        enqueueForecast()

        repository.getForecast(
            izhevsk,
            WeatherSettings(TemperatureUnit.FAHRENHEIT, WindSpeedUnit.KILOMETERS_PER_HOUR)
        )

        val url = server.takeRequest().requestUrl!!
        assertEquals("/v1/forecast", url.encodedPath)
        assertEquals("56.85", url.queryParameter("latitude"))
        assertEquals("16", url.queryParameter("forecast_days"))
        assertEquals("24", url.queryParameter("forecast_hours"))
        assertEquals("fahrenheit", url.queryParameter("temperature_unit"))
        assertEquals("kmh", url.queryParameter("wind_speed_unit"))
        assertEquals("auto", url.queryParameter("timezone"))
    }

    @Test
    fun summary_skipsHourlyDataAndTakesTodayMinMax() = runTest {
        enqueueForecast()

        val summary = repository.getSummary(izhevsk, WeatherSettings())

        val url = server.takeRequest().requestUrl!!
        assertNull(url.queryParameter("hourly"))
        assertEquals("1", url.queryParameter("forecast_days"))
        assertEquals(1.9, summary.todayMax!!, 0.001)
        assertEquals(0.1, summary.todayMin!!, 0.001)
    }
}
