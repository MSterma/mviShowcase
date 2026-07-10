package com.example.mvishowcase.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.mvishowcase.core.ui.R
import com.example.mvishowcase.core.ui.navigator.Navigator
import com.example.mvishowcase.feature.auth.presentation.LoginViewModel
import com.example.mvishowcase.feature.auth.ui.LoginScreen
import com.example.mvishowcase.feature.auth.ui.RegisterScreen
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.feature.home.ui.CountryDetailScreen
import com.example.mvishowcase.feature.home.ui.HomeScreen
import navigator.NavRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(navigator: Navigator) {
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider = { key ->
            when (key) {
                is NavRoute.Login -> NavEntry(key) {
                    val loginViewModel: LoginViewModel = koinViewModel()
                    LoginScreen(
                        viewModel = loginViewModel,
                        onNavigateToHome = { navigator.resetTo(NavRoute.Home) },
                        onNavigateToRegister = { navigator.navigateTo(NavRoute.Register) }
                    )
                }
                is NavRoute.Register -> NavEntry(key) {
                    val loginViewModel: LoginViewModel = koinViewModel()
                    RegisterScreen(
                        viewModel = loginViewModel,
                        onNavigateToHome = { navigator.resetTo(NavRoute.Home) },
                        onBack = { navigator.goBack() }
                    )
                }
                is NavRoute.Home -> NavEntry(key) {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    HomeScreen(viewModel = homeViewModel)
                }
                is NavRoute.Details -> NavEntry(key) {
                    CountryDetailScreen(
                        countryId = key.countryId,
                        onBack = { navigator.goBack() }
                    )
                }
                else -> NavEntry(Unit) { Text(stringResource(R.string.unknown_route)) }
            }
        }
    )
}
