package com.example.mvishowcase.feature.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvishowcase.domain.repository.CountryRepository
import com.example.mvishowcase.domain.usecase.GetCountriesUseCase
import com.example.mvishowcase.feature.home.presentation.HomeViewModel

class HomeContainer(private val countryRepository: CountryRepository) {

    private val getCountriesUseCase: GetCountriesUseCase by lazy {
        GetCountriesUseCase(countryRepository)
    }

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(getCountriesUseCase) as T
        }
    }
}
