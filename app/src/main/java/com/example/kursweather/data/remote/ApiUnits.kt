package com.example.kursweather.data.remote

import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WindSpeedUnit

/** Значения параметров temperature_unit и wind_speed_unit в запросе к Open-Meteo. */
fun TemperatureUnit.toApiValue(): String = when (this) {
    TemperatureUnit.CELSIUS -> "celsius"
    TemperatureUnit.FAHRENHEIT -> "fahrenheit"
}

fun WindSpeedUnit.toApiValue(): String = when (this) {
    WindSpeedUnit.METERS_PER_SECOND -> "ms"
    WindSpeedUnit.KILOMETERS_PER_HOUR -> "kmh"
}
