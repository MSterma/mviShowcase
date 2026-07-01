package com.example.mvishowcase.data.repository

import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.repository.CountryRepository

class FakeCountryRepositoryImpl : CountryRepository {
    private val allCountries = listOf(
        Country("1", "Poland", "🇵🇱", "Warsaw", 38000000),
        Country("2", "Germany", "🇩🇪", "Berlin", 83000000),
        Country("3", "France", "🇫🇷", "Paris", 67000000),
        Country("4", "Italy", "🇮🇹", "Rome", 60000000),
        Country("5", "Spain", "🇪🇸", "Madrid", 47000000)
    )

    override suspend fun searchCountries(query: String, limit: Int, offset: Int): List<Country> {
        val filtered = if (query.isEmpty()) {
            allCountries
        } else {
            allCountries.filter { it.name.contains(query, ignoreCase = true) }
        }
        return filtered.drop(offset).take(limit)
    }
}
