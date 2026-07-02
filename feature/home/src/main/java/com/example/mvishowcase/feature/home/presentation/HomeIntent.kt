package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.mvi.UiIntent
import com.example.mvishowcase.core.model.Country

sealed class HomeIntent : UiIntent {
    data object LoadCountries : HomeIntent()
    data object LoadNextPage : HomeIntent()
    data class SelectCountry(val country: Country) : HomeIntent()
    data class SearchQueryChanged(val query: String) : HomeIntent()
}
