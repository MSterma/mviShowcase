package com.example.mvishowcase.domain.repository

import com.example.mvishowcase.domain.model.Country

interface CountryRepository {
    suspend fun searchCountries(query: String, limit: Int = 25, offset: Int = 0): List<Country>
}
