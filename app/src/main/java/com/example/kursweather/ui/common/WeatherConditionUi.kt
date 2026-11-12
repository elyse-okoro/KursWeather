package com.example.kursweather.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.kursweather.R
import com.example.kursweather.domain.model.WeatherCondition
import com.example.kursweather.ui.theme.CloudIcon
import com.example.kursweather.ui.theme.NightIcon
import com.example.kursweather.ui.theme.RainIcon
import com.example.kursweather.ui.theme.SnowIcon
import com.example.kursweather.ui.theme.StormIcon
import com.example.kursweather.ui.theme.SunnyIcon

@StringRes
fun WeatherCondition.labelRes(): Int = when (this) {
    WeatherCondition.CLEAR -> R.string.condition_clear
    WeatherCondition.MAINLY_CLEAR -> R.string.condition_mainly_clear
    WeatherCondition.PARTLY_CLOUDY -> R.string.condition_partly_cloudy
    WeatherCondition.OVERCAST -> R.string.condition_overcast
    WeatherCondition.FOG -> R.string.condition_fog
    WeatherCondition.DRIZZLE -> R.string.condition_drizzle
    WeatherCondition.FREEZING_DRIZZLE -> R.string.condition_freezing_drizzle
    WeatherCondition.RAIN -> R.string.condition_rain
    WeatherCondition.FREEZING_RAIN -> R.string.condition_freezing_rain
    WeatherCondition.SNOW -> R.string.condition_snow
    WeatherCondition.RAIN_SHOWERS -> R.string.condition_rain_showers
    WeatherCondition.SNOW_SHOWERS -> R.string.condition_snow_showers
    WeatherCondition.THUNDERSTORM -> R.string.condition_thunderstorm
    WeatherCondition.UNKNOWN -> R.string.condition_unknown
}

@DrawableRes
fun WeatherCondition.iconRes(isDay: Boolean = true): Int = when (this) {
    WeatherCondition.CLEAR, WeatherCondition.MAINLY_CLEAR ->
        if (isDay) R.drawable.ic_weather_clear_day else R.drawable.ic_weather_clear_night
    WeatherCondition.PARTLY_CLOUDY -> R.drawable.ic_weather_partly_cloudy
    WeatherCondition.OVERCAST -> R.drawable.ic_weather_cloudy
    WeatherCondition.FOG -> R.drawable.ic_weather_fog
    WeatherCondition.DRIZZLE, WeatherCondition.FREEZING_DRIZZLE -> R.drawable.ic_weather_drizzle
    WeatherCondition.RAIN, WeatherCondition.FREEZING_RAIN, WeatherCondition.RAIN_SHOWERS ->
        R.drawable.ic_weather_rain
    WeatherCondition.SNOW, WeatherCondition.SNOW_SHOWERS -> R.drawable.ic_weather_snow
    WeatherCondition.THUNDERSTORM -> R.drawable.ic_weather_thunderstorm
    WeatherCondition.UNKNOWN -> R.drawable.ic_weather_unknown
}

fun WeatherCondition.iconTint(isDay: Boolean = true): Color = when (this) {
    WeatherCondition.CLEAR, WeatherCondition.MAINLY_CLEAR -> if (isDay) SunnyIcon else NightIcon
    WeatherCondition.PARTLY_CLOUDY, WeatherCondition.OVERCAST, WeatherCondition.FOG,
    WeatherCondition.UNKNOWN -> CloudIcon
    WeatherCondition.DRIZZLE, WeatherCondition.FREEZING_DRIZZLE, WeatherCondition.RAIN,
    WeatherCondition.FREEZING_RAIN, WeatherCondition.RAIN_SHOWERS -> RainIcon
    WeatherCondition.SNOW, WeatherCondition.SNOW_SHOWERS -> SnowIcon
    WeatherCondition.THUNDERSTORM -> StormIcon
}
