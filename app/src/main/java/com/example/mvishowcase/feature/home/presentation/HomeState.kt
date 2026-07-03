package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.base.UiState
import com.example.mvishowcase.domain.model.Country


data class HomeState(
    val uiState: HomeUiState = HomeUiState.Loading,
    val countries: List<Country> = emptyList(),
    val isPaginateLoading: Boolean = false,
    val selectedCountry: Country? = null,
    val searchQuery: String = "",
    val offset: Int = 0,
    val hasReachedEnd: Boolean = false
) : UiState
