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

import io.ktor.client.statement.bodyAsText
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SyncRepositoryImpl(
    private val httpClient: HttpClient,
    private val countryDao: CountryDao,
    private val bearerToken: String
) : SyncRepository {

    private val _syncErrors = MutableSharedFlow<String>()
    override val syncErrors = _syncErrors.asSharedFlow()

    private val _syncResults = MutableSharedFlow<Int>()
    override val syncResults = _syncResults.asSharedFlow()

    override suspend fun syncCountries(query: String, limit: Int, offset: Int): DataResult<Unit> {
        if (bearerToken.isEmpty() || bearerToken == "\"\"") {
            val error = "API key is missing. Please add rest_countries_api_key to local.properties"
            _syncErrors.emit(error)
            return DataResult.Failure(Exception(error))
        }
        return try {
            val endpoint = "https://api.restcountries.com/countries/v5"
            Log.d("SyncRepository", "Syncing countries: query='$query', limit=$limit, offset=$offset")

            val httpResponse = httpClient.get(endpoint) {
                header(HttpHeaders.Authorization, "Bearer $bearerToken")
                url {
                    if (query.isNotEmpty()) {
                        parameters.append("q", query)
                    }
                    parameters.append("limit", limit.toString())
                    parameters.append("offset", offset.toString())
                }
            }
            
            val bodyText = httpResponse.bodyAsText()
            Log.d("SyncRepository", "Raw Response: $bodyText")
            
            if (httpResponse.status.value >= 400) {
                _syncErrors.emit("API error: ${httpResponse.status.description}")
                return DataResult.Failure(Exception("API error"))
            }

            val response: CountryResponse = httpResponse.body()

            val entities = response.data.objects.map { it.toDomain().toEntity() }
            countryDao.upsertCountriesWithMerge(entities)

            _syncResults.emit(entities.size)
            DataResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            _syncErrors.emit(e.message ?: "Synchronization failed")
            DataResult.Failure(e)
        }
    }
}
