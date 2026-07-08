package com.example.mvishowcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mvishowcase.core.ui.R
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.mvishowcase.core.ui.navigator.Navigator
import com.example.mvishowcase.core.ui.theme.MviShowcaseTheme
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.feature.home.ui.CountryDetail
import com.example.mvishowcase.feature.home.ui.HomeScreen
import navigator.NavRoute
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
                                val countryDetail by homeViewModel.getCountryDetailStream(key.countryId)
                                    .collectAsStateWithLifecycle(null)

                                Scaffold(
                                    topBar = {
                                        @OptIn(ExperimentalMaterial3Api::class)
                                        TopAppBar(
                                            title = { Text(stringResource(R.string.country_details_title)) },
                                            navigationIcon = {
                                                IconButton(onClick = {navigator.goBack() }) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_content_description))
                                                }
                                            }
                                        )
                                    }
                                ) { padding ->
                                    Surface(modifier = Modifier.padding(padding).fillMaxSize()) {
                                        countryDetail?.let {
                                            CountryDetail(
                                                country = it,
                                                onBack = { navigator.goBack() }
                                            )
                                        } ?: Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = androidx.compose.ui.Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }
                            else -> NavEntry(Unit) { Text(stringResource(R.string.unknown_route)) }
                        }
                    }
                )
            }
        }
    }
}
