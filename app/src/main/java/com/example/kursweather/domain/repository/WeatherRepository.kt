package com.example.kursweather.domain.repository

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.domain.model.WeatherSummary

interface WeatherRepository {

    suspend fun getForecast(city: City, settings: WeatherSettings): Forecast

    suspend fun getSummary(city: City, settings: WeatherSettings): WeatherSummary
}
