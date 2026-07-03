package com.example.mvishowcase.core.data.di

import com.example.mvishowcase.core.data.repository.CountryRepositoryImpl
import com.example.mvishowcase.core.domain.repository.CountryRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<CountryRepository> { CountryRepositoryImpl(get(), get(named("bearer_token"))) }
}
