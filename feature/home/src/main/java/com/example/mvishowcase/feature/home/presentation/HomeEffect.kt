package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.base.UiEffect

sealed class HomeEffect : UiEffect {
    data class ShowError(val message: String) : HomeEffect()
}
