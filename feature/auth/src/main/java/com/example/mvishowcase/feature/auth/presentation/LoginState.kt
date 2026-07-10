package com.example.mvishowcase.feature.auth.presentation

import com.example.mvishowcase.core.common.base.UiState

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState
