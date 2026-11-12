package com.example.kursweather.ui.fake

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary
import com.example.kursweather.domain.repository.WeatherRepository

class FakeWeatherRepository : WeatherRepository {

    /** Если задано — запросы погоды бросают это исключение. */
    var error: Exception? = null

    /** С какими единицами запрашивали погоду в последний раз. */
    var lastSettings: WeatherSettings? = null
        private set

    var requests = 0
        private set

    override suspend fun getForecast(city: City, settings: WeatherSettings): Forecast {
        requests++
        lastSettings = settings
        error?.let { throw it }
        return TestWeather.forecast(settings)
    }

    override suspend fun getSummary(city: City, settings: WeatherSettings): WeatherSummary {
        requests++
        lastSettings = settings
        error?.let { throw it }
        return TestWeather.summary()
    }
}
