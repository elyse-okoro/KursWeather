package com.example.kursweather.domain

import com.example.kursweather.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherConditionTest {

    @Test
    fun wmoCodes_mapToConditions() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWmoCode(0))
        assertEquals(WeatherCondition.OVERCAST, WeatherCondition.fromWmoCode(3))
        assertEquals(WeatherCondition.FOG, WeatherCondition.fromWmoCode(48))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(63))
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromWmoCode(77))
        assertEquals(WeatherCondition.SNOW_SHOWERS, WeatherCondition.fromWmoCode(86))
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCondition.fromWmoCode(99))
    }

    @Test
    fun unknownOrMissingCode_isUnknown() {
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(null))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(42))
    }
}
