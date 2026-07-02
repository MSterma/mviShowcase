package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.common.mvi.BaseViewModel
import com.example.mvishowcase.core.common.result.DataResult
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
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
            
            when (val result = searchCountriesUseCase(query = query, limit = pageSize, offset = 0)) {
                is DataResult.Success -> {
                    val countries = result.data
                    setState { 
                        copy(
                            uiState = HomeUiState.Success, 
                            countries = countries, 
                            offset = countries.size,
                            hasReachedEnd = countries.size < pageSize
                        ) 
                    }
                }
                is DataResult.Error -> {
                    val errorMessage = result.throwable.message ?: "Unknown error"
                    setState { copy(uiState = HomeUiState.Error(errorMessage)) }
                    sendEffect(HomeEffect.ShowError(errorMessage))
                }
            }
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.uiState == HomeUiState.Loading || currentState.isPaginateLoading || currentState.hasReachedEnd) return

        viewModelScope.launch {
            setState { copy(isPaginateLoading = true) }
            
            when (val result = searchCountriesUseCase(
                query = currentState.searchQuery,
                limit = pageSize, 
                offset = currentState.offset
            )) {
                is DataResult.Success -> {
                    val newCountries = result.data
                    setState { 
                        copy(
                            isPaginateLoading = false, 
                            countries = countries + newCountries, 
                            offset = offset + newCountries.size,
                            hasReachedEnd = newCountries.size < pageSize
                        ) 
                    }
                }
                is DataResult.Error -> {
                    setState { copy(isPaginateLoading = false) }
                    sendEffect(HomeEffect.ShowError(result.throwable.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun selectCountry(country: Country) {
        sendEffect(HomeEffect.NavigateToDetails(country))
    }
}
