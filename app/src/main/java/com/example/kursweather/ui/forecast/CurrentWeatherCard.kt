package com.example.kursweather.ui.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.kursweather.R
import com.example.kursweather.domain.model.CurrentWeather
import com.example.kursweather.domain.model.DailyForecast
import com.example.kursweather.domain.model.WeatherSettings
import com.example.kursweather.ui.common.WeatherIcon
import com.example.kursweather.ui.common.labelRes
import com.example.kursweather.ui.common.pressureText
import com.example.kursweather.ui.common.temperatureText
import com.example.kursweather.ui.common.timeText
import com.example.kursweather.ui.common.windSpeedText

@Composable
fun CurrentWeatherCard(
    current: CurrentWeather,
    today: DailyForecast?,
    settings: WeatherSettings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = temperatureText(current.temperature, settings.temperatureUnit),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = stringResource(current.condition.labelRes()),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.feels_like, temperatureText(current.apparentTemperature)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                WeatherIcon(
                    condition = current.condition,
                    isDay = current.isDay,
                    size = dimensionResource(R.dimen.current_weather_icon_size)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            // сетка 2 × 2: «765 мм рт. ст.» не помещалось в треть ширины и переносилось
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
                WeatherDetail(
                    iconRes = R.drawable.ic_opacity,
                    label = stringResource(R.string.detail_humidity),
                    value = stringResource(R.string.percent_value, current.humidity),
                    modifier = Modifier.weight(1f)
                )
                WeatherDetail(
                    iconRes = R.drawable.ic_air,
                    label = stringResource(R.string.detail_wind),
                    value = windSpeedText(current.windSpeed, settings.windSpeedUnit),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
                WeatherDetail(
                    iconRes = R.drawable.ic_speed,
                    label = stringResource(R.string.detail_pressure),
                    value = pressureText(current.pressureHpa),
                    modifier = Modifier.weight(1f)
                )
                val sunrise = today?.sunrise
                val sunset = today?.sunset
                if (sunrise != null && sunset != null) {
                    WeatherDetail(
                        iconRes = R.drawable.ic_sunrise,
                        label = stringResource(R.string.detail_daylight),
                        value = stringResource(R.string.sunrise_sunset, timeText(sunrise), timeText(sunset)),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeatherDetail(
    @DrawableRes iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.detail_icon_size)),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
