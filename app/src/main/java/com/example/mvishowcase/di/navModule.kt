package com.example.mvishowcase.di

import com.example.mvishowcase.presentation.MainViewModel
import com.example.mvishowcase.core.ui.navigator.Navigator
import com.example.mvishowcase.navigation.NavigatorImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val navModule = module {
    single<Navigator> { NavigatorImpl() }
    viewModelOf(::MainViewModel)
}
