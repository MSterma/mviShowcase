package com.example.mvishowcase.presentation

import androidx.lifecycle.viewModelScope
import com.example.mvishowcase.core.common.base.BaseViewModel
import com.example.mvishowcase.core.domain.usecase.ObserveAuthStateUseCase
import com.example.mvishowcase.core.ui.navigator.Navigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import navigator.NavRoute

class MainViewModel(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val navigator: Navigator
) : BaseViewModel<MainState, MainIntent, MainEffect>(MainState) {

    init {
        observeAuthState()
    }

    override fun onIntent(intent: MainIntent) {
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeAuthStateUseCase().collectLatest { authState ->
                if (authState == null) {
                    if (navigator.backStack.none { it is NavRoute.Login || it is NavRoute.Register }) {
                        navigator.resetTo(NavRoute.Login)
                    }
                } else {
                    if (navigator.backStack.isEmpty() || navigator.backStack.any { it is NavRoute.Login || it is NavRoute.Register }) {
                        navigator.resetTo(NavRoute.Home)
                    }
                }
            }
        }
    }
}
