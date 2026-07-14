package com.example.mvishowcase.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.common.base.BaseViewModel
import com.example.mvishowcase.core.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> setState { copy(email = intent.email) }
            is LoginIntent.PasswordChanged -> setState { copy(password = intent.password) }
            is LoginIntent.SignInClicked -> signIn()
            is LoginIntent.RegisterClicked -> register()
            is LoginIntent.ClearError -> setState { copy(error = null) }
            is LoginIntent.ResetState -> resetState()
        }
    }

    private fun signIn() {
        val email = uiState.value.email
        val password = uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            setState { copy(error = "Email and password cannot be empty") }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            authRepository.signIn(email, password)
                .onSuccess {
                    resetState()
                    sendEffect(LoginEffect.NavigateToHome)
                }
                .onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                    sendEffect(LoginEffect.ShowError(error.message ?: "Sign in failed"))
                }
        }
    }

    private fun register() {
        val email = uiState.value.email
        val password = uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            setState { copy(error = "Email and password cannot be empty") }
            return
        }

        if (password.length < 6) {
            setState { copy(error = "Password must be at least 6 characters") }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            authRepository.signUp(email, password)
                .onSuccess {
                    resetState()
                    sendEffect(LoginEffect.NavigateToHome)
                }
                .onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                    sendEffect(LoginEffect.ShowError(error.message ?: "Registration failed"))
                }
        }
    }
}
