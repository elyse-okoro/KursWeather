package com.example.kursweather.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kursweather.R
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.ui.AppViewModelProvider
import com.example.kursweather.ui.common.LoadingContent
import com.example.kursweather.ui.common.MessageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onCityAdded: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.isCityAdded) {
        if (uiState.isCityAdded) onCityAdded()
    }

    val startSearch = {
        keyboard?.hide()
        viewModel.search()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_search)) },
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
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                label = { Text(text = stringResource(R.string.search_label)) },
                leadingIcon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { startSearch() }),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.search_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
            Button(
                onClick = { startSearch() },
                enabled = uiState.query.isNotBlank() && !uiState.isLoading,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(text = stringResource(R.string.action_search))
            }
            SearchResults(state = uiState, onAdd = viewModel::addCity)
        }
    }
}

/** Ошибки, в которых виноват ключ API, а не сеть и не сервер. */
private val API_KEY_ERRORS = setOf(R.string.error_api_key_missing, R.string.error_api_key_invalid)

@Composable
private fun SearchResults(
    state: SearchUiState,
    onAdd: (FoundCity) -> Unit
) {
    val errorRes = state.errorRes
    val notFoundQuery = state.notFoundQuery
    when {
        state.isLoading -> LoadingContent()
        errorRes != null -> MessageContent(
            iconRes = if (errorRes in API_KEY_ERRORS) R.drawable.ic_key else R.drawable.ic_cloud_off,
            message = stringResource(errorRes)
        )
        notFoundQuery != null -> MessageContent(
            iconRes = R.drawable.ic_location_city,
            message = stringResource(R.string.message_city_not_found, notFoundQuery)
        )
        else -> LazyColumn(
            contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items(items = state.results, key = { "${it.latitude},${it.longitude}" }) { city ->
                FoundCityCard(city = city, onAdd = { onAdd(city) })
            }
        }
    }
}
