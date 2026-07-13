package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.domain.repository.CountryRepository
import com.example.mvishowcase.core.model.Country
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import app.cash.turbine.test

class SearchCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private val useCase = SearchCountriesUseCase(repository)

    @Test
    fun `when query is empty should return all countries from repository`() = runTest {
        val countries = listOf(
            Country("1", "Poland", "flag", "Warsaw", 38000000),
            Country("2", "Germany", "flag", "Berlin", 83000000)
        )
        every { repository.getCountries("") } returns flowOf(countries)

        useCase("").test {
            assertEquals(countries, awaitItem())
            awaitComplete()
        }
    }

}
