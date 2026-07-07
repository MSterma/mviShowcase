package com.example.mvishowcase.core.data.di

import androidx.room.Room
import com.example.mvishowcase.core.data.database.CountryDatabase
import com.example.mvishowcase.core.data.repository.CountryRepositoryImpl
import com.example.mvishowcase.core.data.repository.OfflineFirstCountryRepository
import com.example.mvishowcase.core.data.repository.SyncRepositoryImpl
import com.example.mvishowcase.core.data.sync.SyncWorker
import com.example.mvishowcase.core.domain.repository.CountryRepository
import com.example.mvishowcase.core.domain.repository.SyncRepository
import com.example.mvishowcase.core.domain.usecase.SyncCountriesUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CountryDatabase::class.java,
            "country_db"
        ).build()
    }
    single { get<CountryDatabase>().countryDao() }
    single<CountryRepository> { OfflineFirstCountryRepository(get()) }
    single(named("network_repository")) { CountryRepositoryImpl(get(), get(named("bearer_token"))) }
    single<SyncRepository> { SyncRepositoryImpl(get(), get(), get(named("bearer_token"))) }
    single { SyncCountriesUseCase(androidContext(), SyncWorker::startSync) }
}
