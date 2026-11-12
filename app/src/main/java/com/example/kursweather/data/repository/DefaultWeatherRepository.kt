package com.example.kursweather.data.repository

import com.example.kursweather.data.mapper.toForecast
import com.example.kursweather.data.mapper.toSummary
import com.example.kursweather.data.remote.OpenMeteoService
import com.example.kursweather.data.remote.toApiValue
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary
import com.example.kursweather.domain.repository.WeatherRepository

/**
 * Прогноз из Open-Meteo. Для экрана города — сейчас, 24 часа и 16 дней;
 * для карточки в списке — только текущая погода и минимум/максимум на сегодня.
 */
class DefaultWeatherRepository(
    private val service: OpenMeteoService
) : WeatherRepository {

    override suspend fun getForecast(city: City, settings: WeatherSettings): Forecast =
        service.getForecast(
            latitude = city.latitude,
            longitude = city.longitude,
            current = CURRENT_FIELDS,
            hourly = HOURLY_FIELDS,
            daily = DAILY_FIELDS,
            forecastDays = FORECAST_DAYS,
            forecastHours = FORECAST_HOURS,
            temperatureUnit = settings.temperatureUnit.toApiValue(),
            windSpeedUnit = settings.windSpeedUnit.toApiValue()
        ).toForecast(settings)

    override suspend fun getSummary(city: City, settings: WeatherSettings): WeatherSummary =
        service.getForecast(
            latitude = city.latitude,
            longitude = city.longitude,
            current = CURRENT_FIELDS,
            hourly = null,
            daily = SUMMARY_DAILY_FIELDS,
            forecastDays = 1,
            forecastHours = null,
            temperatureUnit = settings.temperatureUnit.toApiValue(),
            windSpeedUnit = settings.windSpeedUnit.toApiValue()
        ).toSummary()

    private companion object {
        const val CURRENT_FIELDS = "temperature_2m,apparent_temperature,relative_humidity_2m," +
            "weather_code,wind_speed_10m,wind_direction_10m,pressure_msl,is_day"
        const val HOURLY_FIELDS = "temperature_2m,weather_code,precipitation_probability,is_day"
        const val DAILY_FIELDS = "weather_code,temperature_2m_max,temperature_2m_min," +
            "precipitation_probability_max,sunrise,sunset"
        const val SUMMARY_DAILY_FIELDS = "temperature_2m_max,temperature_2m_min"
        const val FORECAST_DAYS = 16
        const val FORECAST_HOURS = 24
    }
}
