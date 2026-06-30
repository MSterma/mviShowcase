package com.example.mvishowcase

import android.app.Application
import com.example.mvishowcase.di.AppContainer

class MviApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
