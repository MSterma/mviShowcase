package com.example.mvishowcase.di

import com.example.mvishowcase.data.repository.FakeCountryRepositoryImpl
import com.example.mvishowcase.domain.repository.CountryRepository

class AppContainer {
    val countryRepository: CountryRepository by lazy {
        FakeCountryRepositoryImpl()
    }
}
