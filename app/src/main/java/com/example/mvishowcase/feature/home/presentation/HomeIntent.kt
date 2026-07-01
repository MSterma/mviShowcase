package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.base.UiIntent
import com.example.mvishowcase.domain.model.Country

sealed class HomeIntent : UiIntent {
    object LoadCountries : HomeIntent()
    data class SelectCountry(val country: Country) : HomeIntent()
    object ClearSelection : HomeIntent()
}
