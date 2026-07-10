package com.example.mvishowcase.feature.auth.di

import com.example.mvishowcase.feature.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::LoginViewModel)
}
