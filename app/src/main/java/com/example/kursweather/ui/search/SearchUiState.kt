package com.example.kursweather.ui.search

import androidx.annotation.StringRes
import com.example.kursweather.domain.model.FoundCity

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<FoundCity> = emptyList(),
    /** Запрос, по которому ничего не нашлось. */
    val notFoundQuery: String? = null,
    @param:StringRes val errorRes: Int? = null,
    /** Город добавлен — экран можно закрывать. */
    val isCityAdded: Boolean = false
)
