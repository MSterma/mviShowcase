package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class SearchCountriesUseCase(private val repository: CountryRepository) {
    operator fun invoke(query: String = ""): Flow<List<Country>> {
        return repository.getCountries(query)
    }
}
