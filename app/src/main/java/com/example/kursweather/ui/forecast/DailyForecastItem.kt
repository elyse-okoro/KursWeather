package com.example.kursweather.ui.forecast

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.kursweather.R
import com.example.kursweather.domain.model.DailyForecast
import com.example.kursweather.ui.common.WeatherIcon
import com.example.kursweather.ui.common.dayText
import com.example.kursweather.ui.common.temperatureText
import java.time.LocalDate

/** Строка дневного прогноза: день, осадки, значок, максимум и минимум. */
@Composable
fun DailyForecastItem(
    day: DailyForecast,
    today: LocalDate,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dayText(day.date, today),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        val precipitation = day.precipitationProbability
        if (precipitation != null && precipitation > 0) {
            Icon(
                painter = painterResource(R.drawable.ic_water_drop),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.precipitation_icon_size))
            )
            Text(
                text = stringResource(R.string.percent_value, precipitation),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.widthIn(min = dimensionResource(R.dimen.precipitation_text_width))
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        WeatherIcon(
            condition = day.condition,
            isDay = true,
            size = dimensionResource(R.dimen.daily_icon_size)
        )
        Text(
            text = temperatureText(day.temperatureMax),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(min = dimensionResource(R.dimen.daily_temperature_width))
        )
        Text(
            text = temperatureText(day.temperatureMin),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(min = dimensionResource(R.dimen.daily_temperature_width))
        )
    }
}
