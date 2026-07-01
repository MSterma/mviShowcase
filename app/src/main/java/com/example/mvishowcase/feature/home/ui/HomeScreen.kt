package com.example.mvishowcase.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.feature.home.presentation.HomeIntent
import com.example.mvishowcase.feature.home.presentation.HomeState
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import androidx.compose.material.icons.filled.Close

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsState()

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
            if (state.selectedCountry != null) {
                TopAppBar(
                    title = { Text("Country Details") },
                    navigationIcon = {
                        IconButton(onClick = { onIntent(HomeIntent.ClearSelection) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            } else {
                Surface(
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            inputField = {
                                SearchBarDefaults.InputField(
                                    query = state.searchQuery,
                                    onQueryChange = { onIntent(HomeIntent.SearchQueryChanged(it)) },
                                    onSearch = {  },
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
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading && state.countries.isEmpty() -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.errorMessage != null && state.countries.isEmpty() -> Text(
                    text = state.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
                state.selectedCountry != null -> CountryDetail(
                    country = state.selectedCountry,
                    onBack = { onIntent(HomeIntent.ClearSelection) }
                )
                else -> CountryList(
                    countries = state.countries,
                    isPaginateLoading = state.isPaginateLoading,
                    onCountryClick = { onIntent(HomeIntent.SelectCountry(it)) },
                    onLoadMore = { onIntent(HomeIntent.LoadNextPage) }
                )
            }
        }
    }
}

@Composable
fun CountryList(
    countries: List<Country>,
    isPaginateLoading: Boolean,
    onCountryClick: (Country) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= countries.size - 5) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(
            items = countries,
            key = { it.id }
        ) { country ->
            ListItem(
                headlineContent = { Text(country.name) },
                leadingContent = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(country.flag)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Flag of ${country.name}",
                        modifier = Modifier.size(40.dp)
                    )
                },
                modifier = Modifier.clickable { onCountryClick(country) }
            )
            HorizontalDivider()
        }

        if (isPaginateLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun CountryDetail(country: Country, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(country.flag)
                .crossfade(true)
                .build(),
            contentDescription = "Flag of ${country.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = country.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Capital: ${country.capital}")
        Text(text = "Population: ${country.population}")
    }
}
