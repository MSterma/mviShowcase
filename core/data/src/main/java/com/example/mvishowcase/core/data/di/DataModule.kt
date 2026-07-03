package com.example.mvishowcase.core.data.di

import com.example.mvishowcase.core.data.repository.CountryRepositoryImpl
import com.example.mvishowcase.core.domain.repository.CountryRepository
import org.koin.dsl.module

val dataModule = module {
    single<CountryRepository> { 
        CountryRepositoryImpl(
            httpClient = get(),
            bearerToken = ""
        )
    }
}
