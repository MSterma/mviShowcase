package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.base.BaseViewModel
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.domain.usecase.SearchCountriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    private val pageSize = 25
    private var searchJob: Job? = null

    init {
        onIntent(HomeIntent.LoadCountries)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadCountries -> loadCountries()
            is HomeIntent.LoadNextPage -> loadNextPage()
            is HomeIntent.SelectCountry -> selectCountry(intent.country)
            is HomeIntent.ClearSelection -> clearSelection()
            is HomeIntent.SearchQueryChanged -> onSearchQueryChanged(intent.query)
        }
    }

    private fun onSearchQueryChanged(query: String) {
        setState { copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200.milliseconds)
            loadCountries()
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            val query = uiState.value.searchQuery
            setState { 
                copy(
                    uiState = HomeUiState.Loading,
                    offset = 0, 
                    hasReachedEnd = false
                ) 
            }
            try {
                val countries = searchCountriesUseCase(query = query, limit = pageSize, offset = 0)
                setState { 
                    copy(
                        uiState = HomeUiState.Success, 
                        countries = countries, 
                        offset = countries.size,
                        hasReachedEnd = countries.size < pageSize
                    ) 
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error"
                setState { copy(uiState = HomeUiState.Error(errorMessage)) }
                sendEffect(HomeEffect.ShowError(errorMessage))
            }
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.uiState == HomeUiState.Loading || currentState.isPaginateLoading || currentState.hasReachedEnd) return

        viewModelScope.launch {
            setState { copy(isPaginateLoading = true) }
            try {
                val newCountries = searchCountriesUseCase(
                    query = currentState.searchQuery,
                    limit = pageSize, 
                    offset = currentState.offset
                )
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
