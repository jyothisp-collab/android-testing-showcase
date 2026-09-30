package com.example.androidtestingshowcase.core.network

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class DemoBackendInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var interceptor: DemoBackendInterceptor

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        interceptor = DemoBackendInterceptor()
    }

    @After
    fun teardown() {
        server.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `intercepts items request and returns valid JSON`() = runTest {
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ShowcaseApi::class.java)
        val items = api.getItems()

        assertEquals(2, items.size)
        assertEquals("testing-patterns", items[0].id)
        assertEquals("Testing Patterns", items[0].title)
        assertEquals("room-caching", items[1].id)
        assertEquals("Room Caching", items[1].title)
    }

    @Test
    fun `intercepts single item request by id`() = runTest {
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ShowcaseApi::class.java)
        val item = api.getItem("testing-patterns")

        assertEquals("testing-patterns", item.id)
        assertEquals("Testing Patterns", item.title)
    }

    @Test
    fun `single item request returns 404 for unknown id`() = runTest {
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ShowcaseApi::class.java)

        var exceptionThrown = false
        try {
            api.getItem("unknown-id")
        } catch (e: Exception) {
            exceptionThrown = true
        }

        assertTrue("Should throw for unknown item id", exceptionThrown)
    }
}
