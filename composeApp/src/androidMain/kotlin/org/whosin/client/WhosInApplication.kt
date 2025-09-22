package org.whosin.client

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.whosin.client.di.initKoin

class WhosInApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WhosInApplication)
        }
    }
}