package com.khanappsnj.nycschools

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class SchoolApp : Application() {

    private val viewModelModule = module {
        single { SchoolViewModel(get()) }
        single { RetrofitClient.createInstance() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            modules(viewModelModule)
        }
    }
}