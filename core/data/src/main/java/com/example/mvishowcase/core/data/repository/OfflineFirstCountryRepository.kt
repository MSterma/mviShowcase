package com.example.mvishowcase.core.data.repository

import com.example.mvishowcase.core.data.database.dao.CountryDao
import com.example.mvishowcase.core.data.mapper.toDomain
import com.example.mvishowcase.core.domain.repository.CountryRepository
import com.example.mvishowcase.core.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstCountryRepository(
    private val countryDao: CountryDao
) : CountryRepository {
    override fun getCountries(query: String): Flow<List<Country>> {
        return countryDao.getCountries(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeCountryById(id: String): Flow<Country?> {
        return countryDao.getCountryByIdFlow(id).map { it?.toDomain() }
    }
}
