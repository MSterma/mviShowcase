package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.mvi.UiEffect
import com.example.mvishowcase.core.model.Country

sealed class HomeEffect : UiEffect {
    data class ShowError(val message: String) : HomeEffect()
    data class NavigateToDetails(val country: Country) : HomeEffect()
}
