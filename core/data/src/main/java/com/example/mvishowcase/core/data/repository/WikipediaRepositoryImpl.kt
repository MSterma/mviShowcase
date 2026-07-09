package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.data.database.dao.CountryDao
import com.example.mvishowcase.core.data.model.WikipediaResponse
import com.example.mvishowcase.core.domain.repository.WikipediaRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WikipediaRepositoryImpl(
    private val httpClient: HttpClient,
    private val countryDao: CountryDao
) : WikipediaRepository {
    override suspend fun refreshCountryDescription(id: String, name: String) {
        try {
            val response: WikipediaResponse = httpClient.get("https://en.wikipedia.org/w/api.php") {
                url {
                    parameters.append("action", "query")
                    parameters.append("exintro", "")
                    parameters.append("explaintext", "")
                    parameters.append("format", "json")
                    parameters.append("prop", "extracts")
                    parameters.append("titles", name)
                }
            }.body()

            val description = response.query?.pages?.values?.firstOrNull()?.extract
            countryDao.updateCountryDescription(id, description)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
