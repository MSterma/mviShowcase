package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.mvi.UiEffect

sealed class HomeEffect : UiEffect {
    data class ShowError(val message: String) : HomeEffect()
}
