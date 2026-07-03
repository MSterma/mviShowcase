package com.example.mvishowcase.core.network

import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient.httpClient }
}
