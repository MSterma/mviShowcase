package com.example.mvishowcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvishowcase.feature.home.di.HomeContainer
import com.example.mvishowcase.feature.home.presentation.HomeViewModel
import com.example.mvishowcase.feature.home.ui.HomeScreen
import com.example.mvishowcase.ui.theme.MviShowcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual DI: Get the AppContainer from the Application class
        val appContainer = (application as MviApplication).appContainer
        
        // Feature-scoped DI: Create the HomeContainer
        val homeContainer = HomeContainer(appContainer.countryRepository)
        
        enableEdgeToEdge()
        setContent {
            MviShowcaseTheme {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = homeContainer.provideHomeViewModelFactory()
                )
                HomeScreen(viewModel = homeViewModel)
            }
        }
    }
}
