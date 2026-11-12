package com.example.kursweather.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.stringResource
import com.example.kursweather.R
import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WindSpeedUnit
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.IllformedLocaleException
import java.util.Locale
import kotlin.math.roundToInt

private const val MINUS_SIGN = '−'
private const val HPA_TO_MM_HG = 0.750062
private val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

/** Температура со знаком, как пишут в прогнозах: «+2°», «−5°», «0°». */
@Composable
fun temperatureText(value: Double): String =
    stringResource(R.string.temperature_value, signedNumber(value))

/** Температура с единицей — «+2°C», «+36°F»: по ней видно, какая шкала выбрана в настройках. */
@Composable
fun temperatureText(value: Double, unit: TemperatureUnit): String {
    val unitLabel = stringResource(
        when (unit) {
            TemperatureUnit.CELSIUS -> R.string.unit_celsius
            TemperatureUnit.FAHRENHEIT -> R.string.unit_fahrenheit
        }
    )
    return stringResource(R.string.temperature_with_unit, signedNumber(value), unitLabel)
}

private fun signedNumber(value: Double): String {
    val rounded = value.roundToInt()
    return when {
        rounded > 0 -> "+$rounded"
        rounded < 0 -> "$MINUS_SIGN${-rounded}"
        else -> "0"
    }
}

@Composable
fun windSpeedText(value: Double, unit: WindSpeedUnit): String = when (unit) {
    WindSpeedUnit.METERS_PER_SECOND ->
        stringResource(R.string.wind_speed_ms, String.format(Locale.getDefault(), "%.1f", value))
    WindSpeedUnit.KILOMETERS_PER_HOUR ->
        stringResource(R.string.wind_speed_kmh, value.roundToInt().toString())
}

/** В русском интерфейсе давление в мм рт. ст., в английском — в гПа. */
@Composable
fun pressureText(hpa: Double): String =
    if (booleanResource(R.bool.pressure_in_mm_hg)) {
        stringResource(R.string.pressure_mm_hg, (hpa * HPA_TO_MM_HG).roundToInt())
    } else {
        stringResource(R.string.pressure_hpa, hpa.roundToInt())
    }

fun timeText(time: LocalTime): String = time.format(TIME_FORMAT)

/** «Сегодня», «Завтра», дальше — короткий день недели и дата. */
@Composable
fun dayText(date: LocalDate, today: LocalDate): String = when (date) {
    today -> stringResource(R.string.day_today)
    today.plusDays(1) -> stringResource(R.string.day_tomorrow)
    else -> date.format(DateTimeFormatter.ofPattern("EEE, d MMM", Locale.getDefault()))
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
}

/** Название страны по двухбуквенному коду ISO на языке телефона: RU → «Россия». */
fun countryName(code: String): String {
    val name = try {
        Locale.Builder().setRegion(code).build().getDisplayCountry(Locale.getDefault())
    } catch (e: IllformedLocaleException) {
        ""
    }
    return name.ifBlank { code }
}
