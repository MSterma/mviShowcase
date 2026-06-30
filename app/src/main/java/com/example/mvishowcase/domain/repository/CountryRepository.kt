package com.example.mvishowcase.domain.repository

import com.example.mvishowcase.domain.model.Country

interface CountryRepository {
    suspend fun getCountries(): List<Country>
}
