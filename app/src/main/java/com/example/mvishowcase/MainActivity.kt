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
        
        val appContainer = (application as MviApplication).appContainer
        
        val homeContainer = HomeContainer(appContainer.countryRepository)
        
        enableEdgeToEdge()
        setContent {
            MviShowcaseTheme {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = homeContainer.viewModelFactory
                )
                HomeScreen(viewModel = homeViewModel)
            }
        }
    }
}
