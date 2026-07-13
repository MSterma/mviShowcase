package com.example.mvishowcase.feature.home.presentation

import app.cash.turbine.test
import com.example.mvishowcase.core.domain.repository.AuthRepository
import com.example.mvishowcase.core.domain.usecase.GetCountryUIDetailUseCase
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.ui.navigator.Navigator
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()
    private val syncCountriesUseCase: SyncCountriesUseCase = mockk()
    private val getCountryUIDetailUseCase: GetCountryUIDetailUseCase = mockk()
    private val authRepository: AuthRepository = mockk()
    private val navigator: Navigator = mockk()

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val searchFlow = MutableSharedFlow<List<Country>>(replay = 1)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { searchCountriesUseCase(any()) } returns searchFlow
        every { syncCountriesUseCase(any(), any(), any()) } just Runs
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
    fun `when search query changed should update state and trigger sync after debounce`() = runTest {
        createViewModel()
        advanceUntilIdle()
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onIntent(HomeIntent.SearchQueryChanged("Pol"))
            val stateWithQuery = awaitItem()
            assertEquals("Pol", stateWithQuery.searchQuery)
            verify(exactly = 1) { syncCountriesUseCase(any(), any(), any()) } // Only the init sync
            advanceTimeBy(201)
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
    fun `when logout intent should call authRepository signOut`() = runTest {
        createViewModel()
        coEvery { authRepository.signOut() } returns Unit

        viewModel.onIntent(HomeIntent.Logout)
        advanceUntilIdle()

        coVerify { authRepository.signOut() }
    }

    @Test
    fun `when load next page and not loading should trigger sync with offset`() = runTest {
        createViewModel()
        advanceUntilIdle()
        
        val countries = listOf(Country("1", "Poland", "flag", "Warsaw", 38000000))
        searchFlow.emit(countries)
        advanceUntilIdle()

        viewModel.uiState.test {
            skipItems(1) // current state (Success)

            viewModel.onIntent(HomeIntent.LoadNextPage)

            val paginateLoadingState = awaitItem()
            assertTrue(paginateLoadingState.isPaginateLoading)
            
            advanceTimeBy(501)
            runCurrent()
            
            val paginateDoneState = awaitItem()
            assertTrue(!paginateDoneState.isPaginateLoading)
            
            verify { syncCountriesUseCase(query = "", limit = 25, offset = 1) }
        }
    }
}
