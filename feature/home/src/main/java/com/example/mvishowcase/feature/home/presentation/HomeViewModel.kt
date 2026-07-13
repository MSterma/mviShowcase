package com.example.mvishowcase.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.common.base.BaseViewModel

import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.domain.repository.AuthRepository
import com.example.mvishowcase.core.domain.usecase.GetCountryUIDetailUseCase
import com.example.mvishowcase.core.domain.usecase.ObserveSyncErrorsUseCase
import com.example.mvishowcase.core.domain.usecase.ObserveSyncResultsUseCase
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import com.example.mvishowcase.core.ui.navigator.Navigator
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import navigator.NavRoute
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val syncCountriesUseCase: SyncCountriesUseCase,
    private val getCountryUIDetailUseCase: GetCountryUIDetailUseCase,
    private val observeSyncErrorsUseCase: ObserveSyncErrorsUseCase,
    private val observeSyncResultsUseCase: ObserveSyncResultsUseCase,
    private val authRepository: AuthRepository,
    private val navigator: Navigator
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    private val pageSize = 25
    private var searchJob: Job? = null
    private var syncJob: Job? = null
    private var observationJob: Job? = null
    private var errorObservationJob: Job? = null
    private var resultObservationJob: Job? = null
    
    private var lastPagedOffset: Int = -1

    init {
        observeSyncErrors()
        observeSyncResults()
        onIntent(HomeIntent.LoadCountries)
    }

    private fun observeSyncErrors() {
        errorObservationJob?.cancel()
        errorObservationJob = viewModelScope.launch {
            observeSyncErrorsUseCase().collect { errorMessage ->
                sendEffect(HomeEffect.ShowError(errorMessage))
                setState { 
                    copy(
                        isPaginateLoading = false,
                        uiState = if (uiState == HomeUiState.Loading) HomeUiState.Success else uiState
                    )
                }
            }
        }
    }

    private fun observeSyncResults() {
        resultObservationJob?.cancel()
        resultObservationJob = viewModelScope.launch {
            observeSyncResultsUseCase().collect { itemsCount ->
                setState { 
                    copy(
                        hasReachedEnd = itemsCount < pageSize,
                        isPaginateLoading = false,
                        uiState = if (uiState == HomeUiState.Loading) HomeUiState.Success else uiState
                    )
                }
            }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        Log.d("HomeViewModel", "OnIntent: $intent")
        when (intent) {
            is HomeIntent.LoadCountries -> {
                observeCountries()
                // Only sync if we have no data (Offline First)
                if (uiState.value.countries.isEmpty()) {
                    syncCountries()
                }
            }
            is HomeIntent.LoadNextPage -> loadNextPage()
            is HomeIntent.SelectCountry -> selectCountry(intent.country)
            is HomeIntent.SearchQueryChanged -> onSearchQueryChanged(intent.query)
            is HomeIntent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            resetState()
        }
    }

    private fun observeCountries() {
        Log.d("HomeViewModel", "observeCountries called")
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            searchCountriesUseCase(uiState.value.searchQuery).collect { countries ->
                Log.d("HomeViewModel", "Received ${countries.size} countries from use case")
                setState { 
                    copy(
                        countries = countries, 
                        uiState = if (uiState == HomeUiState.Loading) HomeUiState.Success else uiState,
                        offset = countries.size
                    )
                }
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        if (uiState.value.searchQuery == query) return
        Log.d("HomeViewModel", "Search query changed to: $query")
        lastPagedOffset = -1
        setState { copy(searchQuery = query, hasReachedEnd = false) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            observeCountries()
            syncCountries()
        }
    }

    private fun syncCountries() {
        if (uiState.value.uiState == HomeUiState.Loading) {
            Log.d("HomeViewModel", "syncCountries: already loading, skipping")
            return
        }

        Log.d("HomeViewModel", "syncCountries: starting sync for query '${uiState.value.searchQuery}'")
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
            
            try {
                syncCountriesUseCase(query = query, limit = pageSize, offset = 0)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "syncCountries failed", e)
                sendEffect(HomeEffect.ShowError(e.message ?: "Failed to trigger sync"))
                setState { copy(uiState = HomeUiState.Success) }
            }
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.uiState == HomeUiState.Loading || currentState.isPaginateLoading || currentState.hasReachedEnd) {
            Log.d("HomeViewModel", "loadNextPage: skipping (loading=${currentState.isPaginateLoading}, end=${currentState.hasReachedEnd})")
            return
        }
        
        if (currentState.offset == lastPagedOffset) {
            Log.d("HomeViewModel", "loadNextPage: skipping (already paged offset ${currentState.offset})")
            return
        }

        Log.d("HomeViewModel", "loadNextPage: starting pagination for offset ${currentState.offset}")
        lastPagedOffset = currentState.offset
        
        viewModelScope.launch {
            setState { copy(isPaginateLoading = true) }
            try {
                syncCountriesUseCase(
                    query = currentState.searchQuery,
                    limit = pageSize, 
                    offset = currentState.offset
                )
            } catch (e: Exception) {
                Log.e("HomeViewModel", "loadNextPage failed", e)
                sendEffect(HomeEffect.ShowError(e.message ?: "Failed to trigger sync"))
                setState { copy(isPaginateLoading = false) }
            }
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
