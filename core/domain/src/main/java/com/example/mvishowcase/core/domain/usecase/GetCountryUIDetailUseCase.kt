package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.domain.repository.CountryRepository
import com.example.mvishowcase.core.model.Country
import kotlinx.coroutines.flow.Flow

class GetCountryUIDetailUseCase(
    private val countryRepository: CountryRepository,
    private val scheduler: WikipediaDescriptionScheduler
) {
    fun refreshDescription(country: Country) {
        scheduler.scheduleRefresh(country.id, country.name)
    }

    operator fun invoke(id: String): Flow<Country?> {
        return countryRepository.observeCountryById(id)
    }
}
