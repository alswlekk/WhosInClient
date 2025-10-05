package org.whosin.client.core.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TokenExpiredManager {
    private val _isTokenExpired = MutableStateFlow(false)
    val isTokenExpired: StateFlow<Boolean> = _isTokenExpired.asStateFlow()

    fun setTokenExpired() {
        _isTokenExpired.value = true
    }

    fun reset() {
        _isTokenExpired.value = false
    }
}