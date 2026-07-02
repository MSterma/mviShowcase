package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.mvi.UiIntent
import com.example.mvishowcase.core.model.Country

sealed class HomeIntent : UiIntent {
    object LoadCountries : HomeIntent()
    object LoadNextPage : HomeIntent()
    data class SelectCountry(val country: Country) : HomeIntent()
    object ClearSelection : HomeIntent()
    data class SearchQueryChanged(val query: String) : HomeIntent()
}
