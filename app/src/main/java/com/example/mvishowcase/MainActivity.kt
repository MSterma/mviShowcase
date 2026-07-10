package com.example.mvishowcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mvishowcase.core.ui.navigator.Navigator
import com.example.mvishowcase.core.ui.theme.MviShowcaseTheme
import com.example.mvishowcase.navigation.AppNavGraph
import com.example.mvishowcase.presentation.MainViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MviShowcaseTheme {
                val navigator: Navigator = koinInject()
                koinViewModel<MainViewModel>()

                if (navigator.backStack.isNotEmpty()) {
                    AppNavGraph(navigator = navigator)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
