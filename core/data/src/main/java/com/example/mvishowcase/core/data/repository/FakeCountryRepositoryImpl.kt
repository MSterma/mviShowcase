package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeCountryRepositoryImpl : CountryRepository {
    private val allCountries = listOf(
        Country("1", "Poland", "https://flagcdn.com/w320/pl.png", "Warsaw", 38000000),
        Country("2", "Germany", "https://flagcdn.com/w320/de.png", "Berlin", 83000000),
        Country("3", "France", "https://flagcdn.com/w320/fr.png", "Paris", 67000000),
        Country("4", "Italy", "https://flagcdn.com/w320/it.png", "Rome", 60000000),
        Country("5", "Spain", "https://flagcdn.com/w320/es.png", "Madrid", 47000000)
    )

    override fun getCountries(query: String): Flow<List<Country>> {
        return flowOf(allCountries).map { countries ->
            if (query.isEmpty()) {
                countries
            } else {
                countries.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
    }

    override fun observeCountryById(id: String): Flow<Country?> {
        return flowOf(allCountries.find { it.id == id })
    }
}
