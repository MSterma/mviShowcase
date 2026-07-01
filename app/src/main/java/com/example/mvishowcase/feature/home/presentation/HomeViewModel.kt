package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.base.BaseViewModel
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.usecase.SearchCountriesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    private val pageSize = 25

    init {
        onIntent(HomeIntent.LoadCountries)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadCountries -> loadCountries()
            is HomeIntent.LoadNextPage -> loadNextPage()
            is HomeIntent.SelectCountry -> selectCountry(intent.country)
            is HomeIntent.ClearSelection -> clearSelection()
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null, offset = 0, hasReachedEnd = false) }
            try {
                val countries = searchCountriesUseCase(limit = pageSize, offset = 0)
                setState { 
                    copy(
                        isLoading = false, 
                        countries = countries, 
                        offset = countries.size,
                        hasReachedEnd = countries.size < pageSize
                    ) 
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, errorMessage = e.message) }
                sendEffect(HomeEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.isLoading || currentState.isPaginateLoading || currentState.hasReachedEnd) return

        viewModelScope.launch {
            setState { copy(isPaginateLoading = true) }
            try {
                val newCountries = searchCountriesUseCase(limit = pageSize, offset = currentState.offset)
                setState { 
                    copy(
                        isPaginateLoading = false, 
                        countries = countries + newCountries, 
                        offset = offset + newCountries.size,
                        hasReachedEnd = newCountries.size < pageSize
                    ) 
                }
            } catch (e: Exception) {
                setState { copy(isPaginateLoading = false) }
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
