package com.example.mvishowcase.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface UiState
interface UiIntent
interface UiEffect

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    private val initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    abstract fun onIntent(intent: I)

    protected fun setState(reducer: S.() -> S) {
        _uiState.update(reducer)
    }

    protected fun resetState() {
        _uiState.value = initialState
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
