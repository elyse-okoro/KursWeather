package com.example.kursweather.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kursweather.R
import com.example.kursweather.domain.model.TemperatureUnit
import com.example.kursweather.domain.model.WindSpeedUnit
import com.example.kursweather.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentWidth()
                .widthIn(max = dimensionResource(R.dimen.content_max_width))
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            SettingsGroup(title = stringResource(R.string.settings_temperature)) {
                OptionRow(
                    text = stringResource(R.string.option_celsius),
                    selected = settings.temperatureUnit == TemperatureUnit.CELSIUS,
                    onSelect = { viewModel.setTemperatureUnit(TemperatureUnit.CELSIUS) }
                )
                OptionRow(
                    text = stringResource(R.string.option_fahrenheit),
                    selected = settings.temperatureUnit == TemperatureUnit.FAHRENHEIT,
                    onSelect = { viewModel.setTemperatureUnit(TemperatureUnit.FAHRENHEIT) }
                )
            }
            SettingsGroup(title = stringResource(R.string.settings_wind)) {
                OptionRow(
                    text = stringResource(R.string.option_meters_per_second),
                    selected = settings.windSpeedUnit == WindSpeedUnit.METERS_PER_SECOND,
                    onSelect = { viewModel.setWindSpeedUnit(WindSpeedUnit.METERS_PER_SECOND) }
                )
                OptionRow(
                    text = stringResource(R.string.option_kilometers_per_hour),
                    selected = settings.windSpeedUnit == WindSpeedUnit.KILOMETERS_PER_HOUR,
                    onSelect = { viewModel.setWindSpeedUnit(WindSpeedUnit.KILOMETERS_PER_HOUR) }
                )
            }
            Text(
                text = stringResource(R.string.settings_data_sources),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_large))
            )
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_small)
        )
    )
    Column(modifier = Modifier.selectableGroup()) {
        content()
    }
}

@Composable
private fun OptionRow(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}
