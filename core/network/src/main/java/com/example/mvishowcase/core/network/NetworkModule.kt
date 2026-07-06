package com.example.mvishowcase.core.network

import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient.httpClient }
    single(named("bearer_token")) { BuildConfig.REST_COUNTRIES_API_KEY }
}
