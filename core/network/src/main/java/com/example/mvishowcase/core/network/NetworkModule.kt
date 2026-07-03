package com.example.mvishowcase.core.network

import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient.httpClient }
    single(named("bearer_token")) { "rc_live_e9ae0cac106a42d08439273200fded44" }
}
