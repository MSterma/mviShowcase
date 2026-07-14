package com.example.mvishowcase.core.domain.di

import com.example.mvishowcase.core.domain.usecase.GetCountryUIDetailUseCase
import com.example.mvishowcase.core.domain.usecase.ObserveAuthStateUseCase
import com.example.mvishowcase.core.domain.usecase.ObserveSyncErrorsUseCase
import com.example.mvishowcase.core.domain.usecase.ObserveSyncResultsUseCase
import com.example.mvishowcase.core.domain.usecase.SearchCountriesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::SearchCountriesUseCase)
    factoryOf(::GetCountryUIDetailUseCase)
    factoryOf(::ObserveAuthStateUseCase)
    factoryOf(::ObserveSyncErrorsUseCase)
    factoryOf(::ObserveSyncResultsUseCase)
}
