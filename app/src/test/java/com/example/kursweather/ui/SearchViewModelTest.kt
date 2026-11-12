package com.example.kursweather.ui

import com.example.kursweather.R
import com.example.kursweather.ui.fake.FakeCityRepository
import com.example.kursweather.ui.search.SearchViewModel
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cities = FakeCityRepository()

    @Test
    fun withoutApiKey_explainsWhatToDo() = runTest {
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = false)

        viewModel.onQueryChange("Izhevsk")
        viewModel.search()

        assertEquals(R.string.error_api_key_missing, viewModel.uiState.value.errorRes)
    }

    @Test
    fun foundCity_canBeAdded() = runTest {
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = true)

        viewModel.onQueryChange("izhevsk")
        viewModel.search()
        val found = viewModel.uiState.value.results.single()
        viewModel.addCity(found)

        assertEquals("Izhevsk", cities.savedCities.single().name)
        assertTrue(viewModel.uiState.value.isCityAdded)
    }

    @Test
    fun unknownCity_showsNotFound() = runTest {
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = true)

        viewModel.onQueryChange("Atlantis")
        viewModel.search()

        assertEquals("Atlantis", viewModel.uiState.value.notFoundQuery)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun networkError_isShownAsNoInternet() = runTest {
        cities.searchError = IOException("offline")
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = true)

        viewModel.onQueryChange("Izhevsk")
        viewModel.search()

        assertEquals(R.string.error_no_internet, viewModel.uiState.value.errorRes)
    }

    @Test
    fun rejectedKey_isShownAsKeyError() = runTest {
        cities.searchError = HttpException(Response.error<Any>(400, "{}".toResponseBody()))
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = true)

        viewModel.onQueryChange("Izhevsk")
        viewModel.search()

        assertEquals(R.string.error_api_key_invalid, viewModel.uiState.value.errorRes)
    }

    @Test
    fun typing_clearsPreviousError() = runTest {
        val viewModel = SearchViewModel(cities, isApiKeyConfigured = false)
        viewModel.onQueryChange("Izhevsk")
        viewModel.search()

        viewModel.onQueryChange("Izhevs")

        assertNull(viewModel.uiState.value.errorRes)
    }
}
