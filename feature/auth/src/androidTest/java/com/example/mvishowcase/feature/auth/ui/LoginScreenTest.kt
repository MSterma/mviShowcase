package com.example.mvishowcase.feature.auth.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mvishowcase.feature.auth.presentation.LoginState
import com.example.mvishowcase.feature.auth.presentation.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: LoginViewModel = mockk(relaxed = true)
    private val uiStateFlow = MutableStateFlow(LoginState())
    private val effectFlow = MutableSharedFlow<com.example.mvishowcase.feature.auth.presentation.LoginEffect>()

    @Test
    fun login_screen_initial_display() {
        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.effect } returns effectFlow

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToHome = {},
                onNavigateToRegister = {}
            )
        }

        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun when_loading_then_progress_indicator_is_displayed() {
        uiStateFlow.value = LoginState(isLoading = true)
        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.effect } returns effectFlow

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToHome = {},
                onNavigateToRegister = {}
            )
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertDoesNotExist()
    }

    @Test
    fun when_error_then_error_text_is_displayed() {
        val errorMessage = "Invalid credentials"
        uiStateFlow.value = LoginState(error = errorMessage)
        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.effect } returns effectFlow

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToHome = {},
                onNavigateToRegister = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}
