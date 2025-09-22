package org.whosin.client

import androidx.compose.ui.window.ComposeUIViewController
import org.whosin.client.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }