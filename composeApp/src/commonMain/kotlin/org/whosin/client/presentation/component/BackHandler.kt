package org.whosin.client.presentation.component

import androidx.compose.runtime.Composable

@Composable
expect fun CommonBackHandler(enabled: Boolean = true, onBack: () -> Unit)