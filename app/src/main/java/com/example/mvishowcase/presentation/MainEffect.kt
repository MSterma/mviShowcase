package com.example.mvishowcase.presentation

import com.example.mvishowcase.core.common.base.UiEffect
import navigator.NavRoute

sealed interface MainEffect : UiEffect {
    data class NavigateTo(val route: NavRoute) : MainEffect
}
