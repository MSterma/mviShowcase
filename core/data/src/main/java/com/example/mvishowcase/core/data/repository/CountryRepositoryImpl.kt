package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.data.mapper.toDomain
import com.example.mvishowcase.core.data.model.CountryResponse
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.CountryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepositoryImpl(
    private val httpClient: HttpClient,
    private val bearerToken: String
) : CountryRepository {

    override fun getCountries(query: String): Flow<List<Country>> = flow {
        try {
            val endpoint = "https://api.restcountries.com/countries/v5"

            val response: CountryResponse = httpClient.get(endpoint) {
                header(HttpHeaders.Authorization, "Bearer $bearerToken")
                url {
                    if (query.isNotEmpty()) {
                        parameters.append("q", query)
                    }
                    parameters.append("limit", "25")
                    parameters.append("offset", "0")
                }
            }.body()

            emit(response.data.objects.map { it.toDomain() })
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    override fun observeCountryById(id: String): Flow<Country?> = flow {
        emit(null)
    }
}
