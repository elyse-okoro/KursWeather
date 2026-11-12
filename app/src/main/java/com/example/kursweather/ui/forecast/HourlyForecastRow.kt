package com.example.kursweather.ui.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.kursweather.R
import com.example.kursweather.domain.model.HourlyForecast
import com.example.kursweather.ui.common.WeatherIcon
import com.example.kursweather.ui.common.temperatureText
import com.example.kursweather.ui.common.timeText

/** Лента на ближайшие 24 часа: время, значок, температура и вероятность осадков. */
@Composable
fun HourlyForecastRow(
    hours: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(items = hours, key = { it.time.toString() }) { hour ->
            HourlyItem(hour)
        }
    }
}

@Composable
private fun HourlyItem(hour: HourlyForecast) {
    OutlinedCard {
        Column(
            modifier = Modifier
                .width(dimensionResource(R.dimen.hourly_item_width))
                .padding(vertical = dimensionResource(R.dimen.padding_small)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
        ) {
            Text(text = timeText(hour.time.toLocalTime()), style = MaterialTheme.typography.labelMedium)
            WeatherIcon(
                condition = hour.condition,
                isDay = hour.isDay,
                size = dimensionResource(R.dimen.hourly_icon_size)
            )
            Text(text = temperatureText(hour.temperature), style = MaterialTheme.typography.titleMedium)
            val precipitation = hour.precipitationProbability
            Text(
                text = if (precipitation != null && precipitation > 0) {
                    stringResource(R.string.percent_value, precipitation)
                } else {
                    stringResource(R.string.no_value)
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
