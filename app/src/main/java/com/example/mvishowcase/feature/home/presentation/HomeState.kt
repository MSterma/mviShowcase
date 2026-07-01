package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.base.UiState
import com.example.mvishowcase.domain.model.Country

data class HomeState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val selectedCountry: Country? = null,
    val errorMessage: String? = null
) : UiState
