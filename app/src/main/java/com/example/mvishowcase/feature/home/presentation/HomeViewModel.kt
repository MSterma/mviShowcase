package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.base.BaseViewModel
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCountriesUseCase: GetCountriesUseCase
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    init {
        onIntent(HomeIntent.LoadCountries)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadCountries -> loadCountries()
            is HomeIntent.SelectCountry -> selectCountry(intent.country)
            is HomeIntent.ClearSelection -> clearSelection()
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            try {
                val countries = getCountriesUseCase()
                setState { copy(isLoading = false, countries = countries) }
            } catch (e: Exception) {
                setState { copy(isLoading = false, errorMessage = e.message) }
                sendEffect(HomeEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun selectCountry(country: Country) {
        setState { copy(selectedCountry = country) }
    }

    private fun clearSelection() {
        setState { copy(selectedCountry = null) }
    }
}
