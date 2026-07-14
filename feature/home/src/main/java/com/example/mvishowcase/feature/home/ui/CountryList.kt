package com.example.mvishowcase.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.mvishowcase.core.ui.R
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mvishowcase.core.model.Country

@Composable
fun CountryList(
    countries: List<Country>,
    isPaginateLoading: Boolean,
    hasReachedEnd: Boolean,
    onCountryClick: (Country) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, isPaginateLoading, hasReachedEnd, countries) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= countries.size - 1 && 
                    !isPaginateLoading && 
                    !hasReachedEnd && 
                    countries.isNotEmpty()
                ) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(
            items = countries,
            key = { it.id }
        ) { country ->
            ListItem(
                headlineContent = { Text(country.name) },
                leadingContent = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(country.flag)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.flag_content_description, country.name),
                        modifier = Modifier.size(40.dp)
                    )
                },
                modifier = Modifier.clickable { onCountryClick(country) }
            )
            HorizontalDivider()
        }

        if (isPaginateLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
