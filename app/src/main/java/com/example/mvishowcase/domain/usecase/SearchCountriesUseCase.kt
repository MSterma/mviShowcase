package com.example.mvishowcase.domain.usecase

import com.example.mvishowcase.core.util.DataResult
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.repository.CountryRepository

class SearchCountriesUseCase(private val repository: CountryRepository) {
    suspend operator fun invoke(query: String = "", limit: Int = 25, offset: Int = 0): DataResult<List<Country>> {
        return repository.searchCountries(query, limit, offset)
    }
}
