package com.example.mvishowcase.core.domain.repository

import com.example.mvishowcase.core.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(query: String): Flow<List<Country>>
    fun observeCountryById(id: String): Flow<Country?>
}
