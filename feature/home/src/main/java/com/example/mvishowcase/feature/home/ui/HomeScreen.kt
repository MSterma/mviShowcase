package com.example.mvishowcase.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mvishowcase.feature.home.presentation.HomeIntent
import com.example.mvishowcase.feature.home.presentation.HomeState
import com.example.mvishowcase.feature.home.presentation.HomeUiState
import com.example.mvishowcase.feature.home.presentation.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = state.searchQuery.isNotEmpty()) {
        viewModel.onIntent(HomeIntent.SearchQueryChanged(""))
    }

    HomeContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
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
                                placeholder = { Text("Search countries...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (state.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onIntent(HomeIntent.SearchQueryChanged("")) }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear")
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
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
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
                        onCountryClick = { onIntent(HomeIntent.SelectCountry(it)) },
                        onLoadMore = { onIntent(HomeIntent.LoadNextPage) }
                    )
                }

                else -> {}
            }
        }
    }
}
