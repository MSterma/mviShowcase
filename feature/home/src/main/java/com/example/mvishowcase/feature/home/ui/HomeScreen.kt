package com.example.mvishowcase.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.example.mvishowcase.core.ui.R
import com.example.mvishowcase.feature.home.presentation.HomeEffect
import com.example.mvishowcase.feature.home.presentation.HomeIntent
import com.example.mvishowcase.feature.home.presentation.HomeState
import com.example.mvishowcase.feature.home.presentation.HomeUiState
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    BackHandler(enabled = state.searchQuery.isNotEmpty()) {
        viewModel.onIntent(HomeIntent.SearchQueryChanged(""))
    }

    HomeContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = state.searchQuery,
                                onQueryChange = { onIntent(HomeIntent.SearchQueryChanged(it)) },
                                onSearch = { },
                                expanded = false,
                                onExpandedChange = { },
                                placeholder = { Text(stringResource(R.string.search_countries_placeholder)) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (state.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onIntent(HomeIntent.SearchQueryChanged("")) }) {
                                            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear_search_content_description))
                                        }
                                    }
                                }
                            )
                        },
                        expanded = false,
                        onExpandedChange = { },
                    ) {
                    }
                }
            }
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { onIntent(HomeIntent.Logout) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { throw RuntimeException("Test Crash for Crashlytics") }
                ) {
                    Icon(Icons.Default.BugReport, contentDescription = stringResource(R.string.crash_app_content_description))
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            
            SnackbarHost(hostState = snackbarHostState)
            
            Box(modifier = Modifier.weight(1f)) {
                when (val uiState = state.uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is HomeUiState.Error -> {
                        Text(
                            text = uiState.message,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is HomeUiState.Success -> {
                        CountryList(
                            countries = state.countries,
                            isPaginateLoading = state.isPaginateLoading,
                            hasReachedEnd = state.hasReachedEnd,
                            onCountryClick = { onIntent(HomeIntent.SelectCountry(it)) },
                            onLoadMore = { onIntent(HomeIntent.LoadNextPage) }
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
