package com.example.mvishowcase.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mvishowcase.feature.auth.presentation.LoginEffect
import com.example.mvishowcase.feature.auth.presentation.LoginIntent
import com.example.mvishowcase.feature.auth.presentation.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(LoginIntent.ResetState)
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onNavigateToHome()
                is LoginEffect.ShowError -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onIntent(LoginIntent.EmailChanged(it)) },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onIntent(LoginIntent.PasswordChanged(it)) },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.onIntent(LoginIntent.SignInClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
