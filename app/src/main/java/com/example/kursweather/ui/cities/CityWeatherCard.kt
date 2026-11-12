package com.example.kursweather.ui.cities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.kursweather.R
import com.example.kursweather.ui.common.WeatherIcon
import com.example.kursweather.ui.common.countryName
import com.example.kursweather.ui.common.labelRes
import com.example.kursweather.ui.common.temperatureText

@Composable
fun CityWeatherCard(
    item: CityWeatherItem,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.city.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = countryName(item.city.country),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                CityWeatherDetails(item)
            }
            val summary = item.summary
            if (summary != null) {
                WeatherIcon(
                    condition = summary.current.condition,
                    isDay = summary.current.isDay,
                    size = dimensionResource(R.dimen.card_weather_icon_size)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = temperatureText(summary.current.temperature),
                    style = MaterialTheme.typography.displaySmall
                )
            } else if (item.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(dimensionResource(R.dimen.small_progress_size)))
            }
            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.action_remove_city, item.city.name),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CityWeatherDetails(item: CityWeatherItem) {
    val summary = item.summary
    val text = when {
        summary != null -> {
            val condition = stringResource(summary.current.condition.labelRes())
            val max = summary.todayMax
            val min = summary.todayMin
            if (max != null && min != null) {
                stringResource(R.string.city_summary, condition, temperatureText(max), temperatureText(min))
            } else {
                condition
            }
        }
        item.isLoading -> stringResource(R.string.message_loading_weather)
        else -> stringResource(R.string.message_weather_unavailable)
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = if (item.hasError && summary == null) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}
