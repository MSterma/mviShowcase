package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.data.database.dao.CountryDao
import com.example.mvishowcase.core.data.mapper.toDomain
import com.example.mvishowcase.core.data.mapper.toEntity
import com.example.mvishowcase.core.data.model.CountryResponse
import com.example.mvishowcase.core.domain.repository.SyncRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class SyncRepositoryImpl(
    private val httpClient: HttpClient,
    private val countryDao: CountryDao,
    private val bearerToken: String
) : SyncRepository {

    override suspend fun syncCountries(query: String, limit: Int, offset: Int): DataResult<Unit> {
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

            val entities = response.data.objects.map { it.toDomain().toEntity() }
            countryDao.upsertCountriesWithMerge(entities)

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }
}
