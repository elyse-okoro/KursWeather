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

    private fun enqueueIzhevsk() {
        val body = javaClass.classLoader!!.getResource("city_izhevsk.json").readText()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
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
