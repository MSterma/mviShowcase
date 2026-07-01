package com.example.mvishowcase.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mvishowcase.domain.model.Country
import com.example.mvishowcase.feature.home.presentation.HomeIntent
import com.example.mvishowcase.feature.home.presentation.HomeState
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.ui.theme.MviShowcaseTheme

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
            TopAppBar(
                title = { Text(if (state.selectedCountry != null) "Country Details" else "Countries") },
                navigationIcon = {
                    if (state.selectedCountry != null) {
                        IconButton(onClick = { onIntent(HomeIntent.ClearSelection) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.errorMessage != null -> Text(
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
                    onCountryClick = { onIntent(HomeIntent.SelectCountry(it)) }
                )
            }
        }
    }
}

@Composable
fun CountryList(countries: List<Country>, onCountryClick: (Country) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(countries) { country ->
            ListItem(
                headlineContent = { Text(country.name) },
                leadingContent = {
                    AsyncImage(
                        model = country.flag,
                        contentDescription = "Flag of ${country.name}",
                        modifier = Modifier.size(40.dp)
                    )
                },
                modifier = Modifier.clickable { onCountryClick(country) }
            )
            HorizontalDivider()
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
            model = country.flag,
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

