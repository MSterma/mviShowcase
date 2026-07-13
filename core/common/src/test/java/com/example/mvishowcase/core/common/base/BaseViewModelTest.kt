package com.example.mvishowcase.core.common.base

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private data class TestState(val count: Int = 0) : UiState
    private sealed interface TestIntent : UiIntent {
        data object Increment : TestIntent
    }
    private sealed interface TestEffect : UiEffect {
        data object Navigation : TestEffect
    }

    private class TestViewModel : BaseViewModel<TestState, TestIntent, TestEffect>(TestState()) {
        override fun onIntent(intent: TestIntent) {
            when (intent) {
                is TestIntent.Increment -> setState { copy(count = count + 1) }
            }
        }

        fun triggerEffect() {
            sendEffect(TestEffect.Navigation)
        }
    }

    private lateinit var viewModel: TestViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is set correctly`() = runTest {
        assertEquals(TestState(), viewModel.uiState.value)
    }

    @Test
    fun `setState updates state correctly`() = runTest {
        viewModel.onIntent(TestIntent.Increment)
        assertEquals(1, viewModel.uiState.value.count)
    }

    @Test
    fun `sendEffect emits effect correctly`() = runTest {
        viewModel.effect.test {
            viewModel.triggerEffect()
            assertEquals(TestEffect.Navigation, awaitItem())
        }
    }
}
