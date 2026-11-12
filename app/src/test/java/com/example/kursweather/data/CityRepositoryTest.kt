package com.example.kursweather.data

import com.example.kursweather.data.remote.ApiKeyInterceptor
import com.example.kursweather.data.remote.CityService
import com.example.kursweather.data.remote.RetrofitFactory
import com.example.kursweather.data.repository.DefaultCityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CityRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var dao: FakeCityDao
    private lateinit var repository: DefaultCityRepository

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val client = OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor("test-key")).build()
        val service = RetrofitFactory.create(server.url("/").toString(), client).create(CityService::class.java)
        dao = FakeCityDao()
        var time = 0L
        repository = DefaultCityRepository(service, dao) { ++time }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    /** Настоящие ответы API Ninjas лежат в test/resources. */
    private fun enqueue(resource: String) {
        val body = javaClass.classLoader!!.getResource(resource).readText()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
    }

    private fun enqueueIzhevsk() = enqueue("city_izhevsk.json")

    private fun enqueueNothingFound() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("[]"))
    }

    @Test
    fun search_sendsApiKeyAndLatinName() = runTest {
        enqueueIzhevsk()

        val found = repository.searchCity("Ижевск")

        val request = server.takeRequest()
        assertEquals("test-key", request.getHeader("X-Api-Key"))
        assertEquals("Izhevsk", request.requestUrl!!.queryParameter("name"))
        assertEquals("Izhevsk", found.single().name)
        assertEquals(56.85, found.single().latitude, 0.0001)
        assertEquals("город нашёлся сразу, геокодинг не нужен", 1, server.requestCount)
    }

    @Test
    fun search_englishNameDiffersFromTransliteration_usesGeocoding() = runTest {
        enqueueNothingFound()
        enqueue("geocoding_moscow.json")

        val found = repository.searchCity("Москва")

        assertEquals("Moskva", server.takeRequest().requestUrl!!.queryParameter("name"))
        val geocoding = server.takeRequest()
        assertEquals("/v1/geocoding", geocoding.requestUrl!!.encodedPath)
        assertEquals("Москва", geocoding.requestUrl!!.queryParameter("city"))
        // только лучший вариант: Москва, а не Moscow в Айдахо
        val moscow = found.single()
        assertEquals("Moscow", moscow.name)
        assertEquals("RU", moscow.country)
        assertEquals(10381222L, moscow.population)
    }

    @Test
    fun search_foundNowhere_returnsEmptyList() = runTest {
        enqueueNothingFound()
        enqueueNothingFound()

        assertTrue(repository.searchCity("Нигдебург").isEmpty())
        assertEquals(2, server.requestCount)
    }

    @Test
    fun search_emptyQuery_doesNotCallApi() = runTest {
        assertTrue(repository.searchCity("   ").isEmpty())
        assertEquals(0, server.requestCount)
    }

    @Test(expected = HttpException::class)
    fun search_wrongKey_isReportedAsHttpError() = runTest {
        server.enqueue(MockResponse().setResponseCode(400).setBody("""{"error": "Invalid API Key."}"""))

        repository.searchCity("Izhevsk")
    }

    @Test
    fun addCity_twice_keepsOneRow() = runTest {
        enqueueIzhevsk()
        val city = repository.searchCity("Izhevsk").single()

        val firstId = repository.addCity(city)
        val secondId = repository.addCity(city)

        assertEquals(firstId, secondId)
        assertEquals(1, repository.observeCities().first().size)
    }

    @Test
    fun addCity_sameCityWithOtherCoordinates_keepsOneRow() = runTest {
        enqueueIzhevsk()
        val fromCityApi = repository.searchCity("Izhevsk").single()
        // геокодинг отдаёт для того же города координаты центра, а не те, что City
        val fromGeocoding = fromCityApi.copy(latitude = 56.8667, longitude = 53.2094)

        val firstId = repository.addCity(fromCityApi)
        val secondId = repository.addCity(fromGeocoding)

        assertEquals(firstId, secondId)
        assertEquals(1, repository.observeCities().first().size)
    }

    @Test
    fun removeAndRestore_returnsCityToItsPlace() = runTest {
        enqueueIzhevsk()
        val city = repository.searchCity("Izhevsk").single()
        val id = repository.addCity(city)
        val saved = repository.getCity(id)!!

        repository.removeCity(id)
        assertTrue(repository.observeCities().first().isEmpty())
        repository.restoreCity(saved)

        assertEquals(listOf(saved), repository.observeCities().first())
    }
}
