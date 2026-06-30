package com.example.mvishowcase.data.repository

import com.example.mvishowcase.data.mapper.toDomain
import com.example.mvishowcase.data.model.CountryResponse
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.repository.CountryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class CountryRepositoryImpl(
    private val httpClient: HttpClient,
    private val bearerToken: String
) : CountryRepository {

    override suspend fun getCountries(): List<Country> {
        val response: CountryResponse = httpClient.get("https://api.restcountries.com/countries/v5") {
            header(HttpHeaders.Authorization, "Bearer $bearerToken")
        }.body()

        return response.data.objects.map { it.toDomain() }
    }
}
