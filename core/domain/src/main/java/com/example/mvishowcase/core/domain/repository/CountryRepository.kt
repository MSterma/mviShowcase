package com.example.mvishowcase.core.domain.repository

import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.model.Country

interface CountryRepository {
    suspend fun searchCountries(query: String, limit: Int, offset: Int): DataResult<List<Country>>
}
