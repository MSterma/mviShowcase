package com.example.mvishowcase.feature.home.presentation

import com.example.mvishowcase.core.common.mvi.UiState
import com.example.mvishowcase.core.model.Country

data class HomeState(
    val uiState: HomeUiState = HomeUiState.Idle,
    val countries: List<Country> = emptyList(),
    val searchQuery: String = "",
    val offset: Int = 0,
    val isPaginateLoading: Boolean = false,
    val hasReachedEnd: Boolean = false
) : UiState
