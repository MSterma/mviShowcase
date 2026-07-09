package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.common.mvi.BaseViewModel

import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.usecase.GetCountryUIDetailUseCase
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import com.example.mvishowcase.core.ui.navigator.Navigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import navigator.NavRoute
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val syncCountriesUseCase: SyncCountriesUseCase,
    private val getCountryUIDetailUseCase: GetCountryUIDetailUseCase,
    private val navigator: Navigator
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    private val pageSize = 25
    private var searchJob: Job? = null
    private var syncJob: Job? = null
    private var observationJob: Job? = null

    init {
        observeCountries()
        onIntent(HomeIntent.LoadCountries)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadCountries -> syncCountries()
            is HomeIntent.LoadNextPage -> loadNextPage()
            is HomeIntent.SelectCountry -> selectCountry(intent.country)
            is HomeIntent.SearchQueryChanged -> onSearchQueryChanged(intent.query)
        }
    }

    private fun observeCountries() {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            searchCountriesUseCase(uiState.value.searchQuery).collectLatest { countries ->
                setState { 
                    copy(
                        countries = countries, 
                        uiState = HomeUiState.Success,
                        offset = countries.size
                    )
                }
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        setState { copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200.milliseconds)
            observeCountries()
            syncCountries()
        }
    }

    private fun syncCountries() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            val query = uiState.value.searchQuery
            setState { 
                copy(
                    uiState = HomeUiState.Loading,
                    offset = 0, 
                    hasReachedEnd = false
                ) 
            }
            
            syncCountriesUseCase(query = query, limit = pageSize, offset = 0)
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.uiState == HomeUiState.Loading || currentState.isPaginateLoading || currentState.hasReachedEnd) return

        viewModelScope.launch {
            setState { copy(isPaginateLoading = true) }
            syncCountriesUseCase(
                query = currentState.searchQuery,
                limit = pageSize, 
                offset = currentState.offset
            )
            delay(500.milliseconds)
            setState { copy(isPaginateLoading = false) }
        }
    }

    private fun selectCountry(country: Country) {
        viewModelScope.launch {
            getCountryUIDetailUseCase.refreshDescription(country)
            navigator.navigateTo(NavRoute.Details(country.id))
        }
    }

    fun getCountryDetailStream(id: String): Flow<Country?> {
        return getCountryUIDetailUseCase(id)
    }
}
