package com.example.kursweather.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.kursweather.R
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.ui.common.countryName
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FoundCityCard(
    city: FoundCity,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = city.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = countryName(city.country),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                city.population?.let { population ->
                    Text(
                        text = stringResource(
                            R.string.city_population,
                            NumberFormat.getIntegerInstance(Locale.getDefault()).format(population)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = stringResource(R.string.city_coordinates, city.latitude, city.longitude),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = onAdd) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
                Text(
                    text = stringResource(R.string.action_add),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}
