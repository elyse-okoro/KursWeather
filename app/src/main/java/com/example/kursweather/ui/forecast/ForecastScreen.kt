package com.example.kursweather.ui.forecast

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kursweather.R
import com.example.kursweather.domain.model.Forecast
import com.example.kursweather.ui.AppViewModelProvider
import com.example.kursweather.ui.common.LoadingContent
import com.example.kursweather.ui.common.MessageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForecastViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    // прогноз уже на экране, а обновить его не удалось: показываем сообщение поверх, старые данные остаются
    val refreshError = uiState.errorRes?.takeIf { uiState.forecast != null }?.let { stringResource(it) }

    LaunchedEffect(refreshError) {
        if (refreshError == null) return@LaunchedEffect
        try {
            snackbarHostState.showSnackbar(refreshError)
        } finally {
            viewModel.onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.city?.name ?: stringResource(R.string.title_forecast)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::retry, enabled = !uiState.isLoading) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(R.string.action_refresh)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val forecast = uiState.forecast
        val errorRes = uiState.errorRes
        when {
            forecast != null -> ForecastContent(
                forecast = forecast,
                isRefreshing = uiState.isLoading,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isLoading -> LoadingContent(modifier = Modifier.padding(innerPadding))
            else -> MessageContent(
                iconRes = R.drawable.ic_cloud_off,
                message = stringResource(errorRes ?: R.string.error_unknown),
                actionLabel = stringResource(R.string.action_retry),
                onAction = viewModel::retry,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ForecastContent(
    forecast: Forecast,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val today = forecast.current.time.toLocalDate()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .wrapContentWidth()
            .widthIn(max = dimensionResource(R.dimen.content_max_width)),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.padding_large))
    ) {
        if (isRefreshing) {
            item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
        }
        item {
            CurrentWeatherCard(
                current = forecast.current,
                today = forecast.daily.firstOrNull { it.date == today },
                settings = forecast.settings,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
        item { SectionTitle(text = stringResource(R.string.section_hourly)) }
        item { HourlyForecastRow(hours = forecast.hourly) }
        item { SectionTitle(text = pluralStringResource(R.plurals.section_daily, forecast.daily.size, forecast.daily.size)) }
        items(items = forecast.daily, key = { it.date.toString() }) { day ->
            DailyForecastItem(day = day, today = today)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
            start = dimensionResource(R.dimen.padding_medium),
            end = dimensionResource(R.dimen.padding_medium),
            top = dimensionResource(R.dimen.padding_large),
            bottom = dimensionResource(R.dimen.padding_small)
        )
    )
}
