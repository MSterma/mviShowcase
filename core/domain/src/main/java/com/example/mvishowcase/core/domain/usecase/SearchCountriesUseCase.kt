package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.CountryRepository

class SearchCountriesUseCase(private val repository: CountryRepository) {
    suspend operator fun invoke(query: String = "", limit: Int = 25, offset: Int = 0): DataResult<List<Country>> {
        return repository.searchCountries(query, limit, offset)
    }
}
