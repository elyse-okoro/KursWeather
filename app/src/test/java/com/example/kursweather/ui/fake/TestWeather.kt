package com.example.kursweather.ui.fake

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.CurrentWeather
import com.example.kursweather.domain.model.DailyForecast
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.domain.model.HourlyForecast
import com.example.kursweather.domain.model.WeatherCondition
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Готовые данные для тестов ViewModel. */
object TestWeather {
    val izhevsk = FoundCity("Izhevsk", "RU", 56.85, 53.2333, 623472, false)
    val kazan = FoundCity("Kazan", "RU", 55.7887, 49.1221, 1308660, false)

    private val now = LocalDateTime.of(2026, 11, 12, 13, 0)

    fun current(temperature: Double = 1.8) = CurrentWeather(
        time = now,
        temperature = temperature,
        apparentTemperature = temperature - 3,
        humidity = 89,
        windSpeed = 2.2,
        windDirection = 190,
        pressureHpa = 1020.4,
        condition = WeatherCondition.OVERCAST,
        isDay = true
    )

    fun summary(temperature: Double = 1.8) = WeatherSummary(current(temperature), todayMax = 1.9, todayMin = 0.1)

    fun forecast(settings: WeatherSettings) = Forecast(
        current = current(),
        hourly = (0 until 24).map { hour ->
            HourlyForecast(now.plusHours(hour.toLong()), 1.0, WeatherCondition.SNOW, 50, true)
        },
        daily = (0 until 16).map { day ->
            DailyForecast(
                LocalDate.of(2026, 11, 12).plusDays(day.toLong()), WeatherCondition.SNOW,
                2.0, -1.0, 40, LocalTime.of(8, 0), LocalTime.of(16, 20)
            )
        },
        settings = settings
    )

    fun city(id: Long, found: FoundCity) = City(id, found.name, found.country, found.latitude, found.longitude)
}
