package com.example.mvishowcase.feature.home.presentation

import app.cash.turbine.test
import com.example.mvishowcase.core.domain.repository.AuthRepository
import com.example.mvishowcase.core.domain.usecase.*
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.ui.navigator.Navigator
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import com.example.mvishowcase.core.common.base.BaseViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()
    private val syncCountriesUseCase: SyncCountriesUseCase = mockk()
    private val getCountryUIDetailUseCase: GetCountryUIDetailUseCase = mockk()
    private val observeSyncErrorsUseCase: ObserveSyncErrorsUseCase = mockk()
    private val observeSyncResultsUseCase: ObserveSyncResultsUseCase = mockk()
    private val authRepository: AuthRepository = mockk()
    private val navigator: Navigator = mockk()

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val searchFlow = MutableSharedFlow<List<Country>>(replay = 1)
    private val errorFlow = MutableSharedFlow<String>()
    private val resultFlow = MutableSharedFlow<Int>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { searchCountriesUseCase(any()) } returns searchFlow
        every { syncCountriesUseCase(any(), any(), any()) } just Runs
        every { observeSyncErrorsUseCase() } returns errorFlow
        every { observeSyncResultsUseCase() } returns resultFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = HomeViewModel(
            searchCountriesUseCase,
            syncCountriesUseCase,
            getCountryUIDetailUseCase,
            observeSyncErrorsUseCase,
            observeSyncResultsUseCase,
            authRepository,
            navigator
        )
    }

    @Test
    fun `initial state is correct and triggers initial sync`() = runTest {
        createViewModel()
        
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(HomeUiState.Idle, initialState.uiState)
            
            advanceUntilIdle()
            
            val loadingState = awaitItem()
            assertEquals(HomeUiState.Loading, loadingState.uiState)
            
            verify { syncCountriesUseCase(query = "", limit = 25, offset = 0) }
            
            val countries = listOf(Country("1", "Poland", "flag", "Warsaw", 38000000))
            searchFlow.emit(countries)
            
            val successState = awaitItem()
            assertEquals(HomeUiState.Success, successState.uiState)
            assertEquals(countries, successState.countries)
        }
    }

    @Test
    fun `when sync error occurs should send error effect`() = runTest {
        createViewModel()
        advanceUntilIdle() 
        
        val errorMessage = "API doesn't work"
        
        viewModel.effect.test {
            errorFlow.emit(errorMessage)
            
            val effect = awaitItem()
            assertTrue(effect is HomeEffect.ShowError)
            assertEquals(errorMessage, (effect as HomeEffect.ShowError).message)
        }
    }

    @Test
    fun `when search query changed should update state and trigger sync after debounce`() = runTest {
        createViewModel()
        advanceUntilIdle()
        
        viewModel.uiState.test {
            skipItems(1) 

            viewModel.onIntent(HomeIntent.SearchQueryChanged("Pol"))
            val stateWithQuery = awaitItem()
            assertEquals("Pol", stateWithQuery.searchQuery)
            
            advanceTimeBy(501)
            runCurrent()
            
            verify(exactly = 2) { syncCountriesUseCase(any(), any(), any()) }
            verify { syncCountriesUseCase(query = "Pol", limit = 25, offset = 0) }
        }
    }

    @Test
    fun `when country selected should refresh description and navigate`() = runTest {
        createViewModel()
        val country = Country("1", "Poland", "flag", "Warsaw", 38000000)
        
        every { getCountryUIDetailUseCase.refreshDescription(any()) } just Runs
        every { navigator.navigateTo(any()) } just Runs

        viewModel.onIntent(HomeIntent.SelectCountry(country))
        advanceUntilIdle()

        verify { getCountryUIDetailUseCase.refreshDescription(country) }
        verify { navigator.navigateTo(any()) }
    }

    @Test
    fun `when logout intent should call authRepository signOut and reset state`() = runTest {
        createViewModel()
        coEvery { authRepository.signOut() } returns Unit
        
        viewModel.onIntent(HomeIntent.SearchQueryChanged("Pol"))
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.Logout)
        advanceUntilIdle()

        coVerify { authRepository.signOut() }
        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `when LoadCountries intent and data exists should not trigger sync`() = runTest {
        val countries = listOf(Country("1", "Poland", "flag", "Warsaw", 38000000))
        searchFlow.emit(countries)
        
        createViewModel()
        advanceUntilIdle()
        
        assertEquals(countries, viewModel.uiState.value.countries)
        
        clearMocks(syncCountriesUseCase)
        viewModel.onIntent(HomeIntent.LoadCountries)
        advanceUntilIdle()

        verify(exactly = 0) { syncCountriesUseCase(any(), any(), any()) }
    }

    @Test
    fun `when sync result items less than page size should set hasReachedEnd and reset loading`() = runTest {
        createViewModel()
        advanceUntilIdle()
        
        // This sets uiState to Success by default because searchFlow emits empty or something
        // But LoadCountries was called in init, which calls syncCountries, which sets uiState = Loading
        
        resultFlow.emit(10)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state.hasReachedEnd)
        assertFalse(state.isPaginateLoading)
        assertEquals(HomeUiState.Success, state.uiState)
    }

    @Test
    fun `when sync error occurs should reset loading states`() = runTest {
        createViewModel()
        advanceUntilIdle()
        
        // Manually trigger a loading state if possible or just emit error
        errorFlow.emit("Error")
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.isPaginateLoading)
        assertEquals(HomeUiState.Success, state.uiState)
    }
}
