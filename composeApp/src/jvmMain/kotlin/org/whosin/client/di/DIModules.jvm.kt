package org.whosin.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.core.datastore.TokenManagerImpl
import org.whosin.client.datastore.createDataStore

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single<DataStore<Preferences>> { createDataStore() }
        single<TokenManager> { TokenManagerImpl(get()) }
    }
