package com.example.mvishowcase.feature.auth.presentation

import app.cash.turbine.test
import com.example.mvishowcase.core.domain.repository.AuthRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals("", viewModel.uiState.value.email)
        assertEquals("", viewModel.uiState.value.password)
        assertNull(viewModel.uiState.value.error)
        assertTrue(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `when email changed then state updates correctly`() = runTest {
        viewModel.onIntent(LoginIntent.EmailChanged("test@example.com"))
        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `when password changed then state updates correctly`() = runTest {
        viewModel.onIntent(LoginIntent.PasswordChanged("password123"))
        assertEquals("password123", viewModel.uiState.value.password)
    }

    @Test
    fun `when sign in with empty fields then show error`() = runTest {
        viewModel.onIntent(LoginIntent.SignInClicked)
        assertEquals("Email and password cannot be empty", viewModel.uiState.value.error)
    }

    @Test
    fun `when sign in successful then navigate to home`() = runTest {
        val email = "test@example.com"
        val password = "password"
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.PasswordChanged(password))
        
        coEvery { authRepository.signIn(email, password) } returns Result.success(Unit)

        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.SignInClicked)
            
            // Advance until idle to handle the viewModelScope.launch
            advanceUntilIdle()
            
            assertEquals(LoginEffect.NavigateToHome, awaitItem())
            assertTrue(!viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `when sign in fails then show error`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val errorMessage = "Invalid credentials"
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.PasswordChanged(password))
        
        coEvery { authRepository.signIn(email, password) } returns Result.failure(Exception(errorMessage))

        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.SignInClicked)
            
            advanceUntilIdle()
            
            val effect = awaitItem()
            assertTrue(effect is LoginEffect.ShowError)
            assertEquals(errorMessage, (effect as LoginEffect.ShowError).message)
            assertEquals(errorMessage, viewModel.uiState.value.error)
            assertTrue(!viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `when register with short password then show error`() = runTest {
        viewModel.onIntent(LoginIntent.EmailChanged("test@example.com"))
        viewModel.onIntent(LoginIntent.PasswordChanged("123"))
        
        viewModel.onIntent(LoginIntent.RegisterClicked)
        
        assertEquals("Password must be at least 6 characters", viewModel.uiState.value.error)
    }
}
