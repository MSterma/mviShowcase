package com.example.mvishowcase.feature.auth.presentation

import com.example.mvishowcase.core.common.base.UiEffect

sealed interface LoginEffect : UiEffect {
    data object NavigateToHome : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
