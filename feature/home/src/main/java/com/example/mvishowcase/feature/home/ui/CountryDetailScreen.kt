package com.example.mvishowcase.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mvishowcase.core.model.Country
import com.example.mvishowcase.core.ui.R

@Composable
fun CountryDetail(country: Country, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(country.flag)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.flag_content_description, country.name),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = country.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Capital: ${country.capital}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Population: ${country.population}",
            style = MaterialTheme.typography.bodyLarge
        )

        country.description?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                lineHeight = 20.sp
            )
        }
    }
}
