package com.example.mvishowcase

import android.app.Application
import com.example.mvishowcase.core.data.di.dataModule
import com.example.mvishowcase.core.domain.di.domainModule
import com.example.mvishowcase.core.network.networkModule
import com.example.mvishowcase.feature.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MviApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MviApplication)
            modules(
                networkModule,
                dataModule,
                domainModule,
                homeModule
            )
        }
    }
}
