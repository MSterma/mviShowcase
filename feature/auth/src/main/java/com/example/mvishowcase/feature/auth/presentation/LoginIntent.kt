package com.example.mvishowcase.feature.auth.presentation

import com.example.mvishowcase.core.common.base.UiIntent

sealed interface LoginIntent : UiIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object SignInClicked : LoginIntent
    data object RegisterClicked : LoginIntent
    data object ClearError : LoginIntent
    data object ResetState : LoginIntent
}
