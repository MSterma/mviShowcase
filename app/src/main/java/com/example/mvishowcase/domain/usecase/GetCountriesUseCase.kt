package com.example.mvishowcase.domain.usecase

import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.repository.CountryRepository

class GetCountriesUseCase(private val repository: CountryRepository) {
    suspend operator fun invoke(): List<Country> {
        return repository.getCountries()
    }
}
