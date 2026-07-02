package com.example.mvishowcase.feature.home.presentation
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data object Success : HomeUiState
}
