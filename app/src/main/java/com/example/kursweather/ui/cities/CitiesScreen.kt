package com.example.kursweather.ui.cities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kursweather.R
import com.example.kursweather.domain.model.City
import com.example.kursweather.ui.AppViewModelProvider
import com.example.kursweather.ui.common.LoadingContent
import com.example.kursweather.ui.common.MessageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onAddCity: () -> Unit,
    onOpenCity: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val removedCity = uiState.removedCity
    val removedMessage = removedCity?.let { stringResource(R.string.message_city_removed, it.name) }
    val undoLabel = stringResource(R.string.action_undo)

    LaunchedEffect(removedCity) {
        if (removedMessage == null) return@LaunchedEffect
        var isUndone = false
        try {
            val result = snackbarHostState.showSnackbar(
                message = removedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short
            )
            isUndone = result == SnackbarResult.ActionPerformed
        } finally {
            // срабатывает и когда экран закрыли раньше, чем сообщение исчезло: второй раз его не покажем
            if (isUndone) viewModel.undoRemove() else viewModel.onRemovedMessageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.action_settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddCity,
                // текст расширенной кнопки Material 3 скрывает от TalkBack, поэтому подпись — у значка
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.action_add_city)
                    )
                },
                text = { Text(text = stringResource(R.string.action_add_city)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                !uiState.isLoaded -> LoadingContent()
                uiState.cities.isEmpty() -> MessageContent(
                    iconRes = R.drawable.ic_location_city,
                    message = stringResource(R.string.message_no_cities),
                    actionLabel = stringResource(R.string.action_add_city),
                    onAction = onAddCity
                )
                else -> CityList(
                    items = uiState.cities,
                    onOpenCity = onOpenCity,
                    onRemoveCity = viewModel::removeCity
                )
            }
        }
    }
}

@Composable
private fun CityList(
    items: List<CityWeatherItem>,
    onOpenCity: (Long) -> Unit,
    onRemoveCity: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .wrapContentWidth()
            .widthIn(max = dimensionResource(R.dimen.content_max_width)),
        contentPadding = PaddingValues(
            start = dimensionResource(R.dimen.padding_medium),
            end = dimensionResource(R.dimen.padding_medium),
            top = dimensionResource(R.dimen.padding_small),
            bottom = dimensionResource(R.dimen.list_bottom_space)
        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(items = items, key = { it.city.id }) { item ->
            CityWeatherCard(
                item = item,
                onClick = { onOpenCity(item.city.id) },
                onRemove = { onRemoveCity(item.city) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
