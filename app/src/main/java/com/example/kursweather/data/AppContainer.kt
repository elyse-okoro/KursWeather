package com.example.kursweather.data

import com.example.kursweather.domain.repository.CityRepository
import com.example.kursweather.domain.repository.SettingsRepository
import com.example.kursweather.domain.repository.WeatherRepository

/** Зависимости уровня приложения, внедряются вручную через WeatherApplication. */
interface AppContainer {
    val cityRepository: CityRepository
    val weatherRepository: WeatherRepository
    val settingsRepository: SettingsRepository

    /** false, если в local.properties не задан ключ API Ninjas и поиск городов не заработает. */
    val isCityApiConfigured: Boolean
}
