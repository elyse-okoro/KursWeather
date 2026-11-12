package com.example.kursweather.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursweather.R
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.domain.repository.CityRepository
import com.example.kursweather.ui.common.citySearchErrorRes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val cityRepository: CityRepository,
    private val isApiKeyConfigured: Boolean
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(text: String) {
        _uiState.update { it.copy(query = text, errorRes = null, notFoundQuery = null) }
    }

    fun search() {
        val text = _uiState.value.query.trim()
        if (text.isEmpty()) return
        if (!isApiKeyConfigured) {
            _uiState.update { it.copy(errorRes = R.string.error_api_key_missing, results = emptyList()) }
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorRes = null, notFoundQuery = null, results = emptyList())
            }
            try {
                val found = cityRepository.searchCity(text)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        results = found,
                        notFoundQuery = if (found.isEmpty()) text else null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w(TAG, "City search failed", e)
                _uiState.update { it.copy(isLoading = false, errorRes = e.citySearchErrorRes()) }
            }
        }
    }

    fun addCity(city: FoundCity) {
        viewModelScope.launch {
            cityRepository.addCity(city)
            _uiState.update { it.copy(isCityAdded = true) }
        }
    }

    private companion object {
        const val TAG = "SearchViewModel"
    }
}
