package com.example.mvishowcase.core.data.repository

import app.cash.turbine.test
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryRepositoryImplTest {

    private fun createMockClient(jsonResponse: String, status: HttpStatusCode = HttpStatusCode.OK): HttpClient {
        val mockEngine = MockEngine { request ->
            respond(
                content = jsonResponse,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `when getCountries returns success then it maps to domain correctly`() = runTest {
        val jsonResponse = """
            {
                "data": {
                    "objects": [
                        {
                            "uuid": "1",
                            "names": { "common": "Poland" },
                            "flag": { "url_png": "flag_url" },
                            "capitals": [
                                { 
                                    "name": "Warsaw",
                                    "attributes": { "primary": true }
                                }
                            ],
                            "population": 38000000
                        }
                    ]
                }
            }
        """.trimIndent()

        val repository = CountryRepositoryImpl(createMockClient(jsonResponse), "token")

        repository.getCountries("").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("1", result[0].id)
            assertEquals("Poland", result[0].name)
            assertEquals("Warsaw", result[0].capital)
            awaitComplete()
        }
    }

    @Test
    fun `when getCountries returns error then it emits empty list`() = runTest {
        val repository = CountryRepositoryImpl(
            createMockClient("Error", HttpStatusCode.InternalServerError),
            "token"
        )

        repository.getCountries("").test {
            val result = awaitItem()
            assertEquals(0, result.size)
            awaitComplete()
        }
    }
}
