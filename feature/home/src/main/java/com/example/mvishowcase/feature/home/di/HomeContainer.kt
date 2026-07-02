package com.example.mvishowcase.feature.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvishowcase.core.domain.repository.CountryRepository
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import com.example.mvishowcase.feature.home.presentation.HomeViewModel

class HomeContainer(private val countryRepository: CountryRepository) {

    private val searchCountriesUseCase: SearchCountriesUseCase by lazy {
        SearchCountriesUseCase(countryRepository)
    }

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(searchCountriesUseCase) as T
        }
    }
}
