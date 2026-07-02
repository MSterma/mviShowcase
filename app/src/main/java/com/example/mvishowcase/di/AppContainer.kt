package com.example.mvishowcase.di

import com.example.mvishowcase.core.network.NetworkClient
import com.example.mvishowcase.data.repository.CountryRepositoryImpl
import com.example.mvishowcase.domain.repository.CountryRepository

class AppContainer {
    val networkClient = NetworkClient
    val countryRepository: CountryRepository by lazy {
        CountryRepositoryImpl(
            httpClient = networkClient.httpClient,
            bearerToken = "rc_live_e9ae0cac106a42d08439273200fded44"
        )
    }
}
