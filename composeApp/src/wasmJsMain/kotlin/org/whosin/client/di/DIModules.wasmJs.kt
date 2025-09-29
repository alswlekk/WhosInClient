package org.whosin.client.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.Module
import org.koin.dsl.module
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.datastore.JsTokenManagerImpl

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { CIO.create() }
        single<TokenManager> { JsTokenManagerImpl() }
    }
