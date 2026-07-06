package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.data.mapper.toDomain
import com.example.mvishowcase.core.data.model.CountryResponse
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.CountryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class CountryRepositoryImpl(
    private val httpClient: HttpClient,
    private val bearerToken: String
) : CountryRepository {

    override suspend fun searchCountries(query: String, limit: Int, offset: Int): DataResult<List<Country>> {
        return try {
            val endpoint = "https://api.restcountries.com/countries/v5"

            val response: CountryResponse = httpClient.get(endpoint) {
                header(HttpHeaders.Authorization, "Bearer $bearerToken")
                url {
                    if (query.isNotEmpty()) {
                        parameters.append("q", query)
                    }
                    parameters.append("limit", limit.toString())
                    parameters.append("offset", offset.toString())
                }
            }.body()

            DataResult.Success(response.data.objects.map { it.toDomain() })
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }
}
