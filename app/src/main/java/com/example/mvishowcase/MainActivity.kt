package com.example.mvishowcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.mvishowcase.core.ui.theme.MviShowcaseTheme
import com.example.mvishowcase.feature.home.di.HomeContainer
import com.example.mvishowcase.feature.home.presentation.HomeEffect
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.feature.home.ui.CountryDetail
import com.example.mvishowcase.feature.home.ui.HomeScreen
import com.example.mvishowcase.navigation.NavRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as MviApplication).appContainer
        val homeContainer = HomeContainer(appContainer.countryRepository)

        enableEdgeToEdge()
        setContent {
            MviShowcaseTheme {
                val backStack = remember { mutableStateListOf<Any>(NavRoute.Home) }
                val homeViewModel: HomeViewModel = viewModel(
                    factory = homeContainer.viewModelFactory
                )

                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        } else {
                            finish()
                        }
                    },
                    entryProvider = { key ->
                        when (key) {
                            is NavRoute.Home -> NavEntry(key) {
                                LaunchedEffect(homeViewModel) {
                                    homeViewModel.effect.collect { effect ->
                                        when (effect) {
                                            is HomeEffect.NavigateToDetails -> {
                                                backStack.add(NavRoute.Details(effect.country))
                                            }
                                            else -> { /* Other effects handled inside screen if needed */ }
                                        }
                                    }
                                }

                                HomeScreen(viewModel = homeViewModel)
                            }
                            is NavRoute.Details -> NavEntry(key) {
                                Scaffold(
                                    topBar = {
                                        @OptIn(ExperimentalMaterial3Api::class)
                                        TopAppBar(
                                            title = { Text("Country Details") },
                                            navigationIcon = {
                                                IconButton(onClick = { backStack.removeAt(backStack.lastIndex) }) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                                }
                                            }
                                        )
                                    }
                                ) { padding ->
                                    Surface(modifier = Modifier.padding(padding).fillMaxSize()) {
                                        CountryDetail(
                                            country = key.country,
                                            onBack = { backStack.removeAt(backStack.lastIndex) }
                                        )
                                    }
                                }
                            }
                            else -> NavEntry(Unit) { Text("Unknown Route") }
                        }
                    }
                )
            }
        }
    }
}
