package com.example.mvishowcase.di

import com.example.mvishowcase.core.network.NetworkClient
import com.example.mvishowcase.core.data.repository.CountryRepositoryImpl
import com.example.mvishowcase.core.domain.repository.CountryRepository

class AppContainer {
    val networkClient = NetworkClient
    val countryRepository: CountryRepository by lazy {
        CountryRepositoryImpl(
            httpClient = networkClient.httpClient,
            bearerToken = ""
        )
    }
}
