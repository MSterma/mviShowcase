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
import com.example.mvishowcase.core.ui.navigator.Navigator
import com.example.mvishowcase.core.ui.theme.MviShowcaseTheme
import com.example.mvishowcase.feature.home.presentation.HomeEffect
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.feature.home.ui.CountryDetail
import com.example.mvishowcase.feature.home.ui.HomeScreen
import navigator.NavRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MviShowcaseTheme {
                val navigator: Navigator= koinInject()
                val homeViewModel: HomeViewModel = getViewModel<HomeViewModel>()

                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = {navigator.goBack()},
                    entryProvider = { key ->
                        when (key) {
                            is NavRoute.Home -> NavEntry(key) {

                                HomeScreen(viewModel = homeViewModel)
                            }
                            is NavRoute.Details -> NavEntry(key) {
                                Scaffold(
                                    topBar = {
                                        @OptIn(ExperimentalMaterial3Api::class)
                                        TopAppBar(
                                            title = { Text("Country Details") },
                                            navigationIcon = {
                                                IconButton(onClick = {navigator.goBack() }) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                                }
                                            }
                                        )
                                    }
                                ) { padding ->
                                    Surface(modifier = Modifier.padding(padding).fillMaxSize()) {
                                        CountryDetail(
                                            country = key.country,
                                            onBack = { navigator.goBack() }
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
