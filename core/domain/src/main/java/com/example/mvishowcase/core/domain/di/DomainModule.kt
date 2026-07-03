package com.example.mvishowcase.core.domain.di

import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SearchCountriesUseCase(get()) }
}
