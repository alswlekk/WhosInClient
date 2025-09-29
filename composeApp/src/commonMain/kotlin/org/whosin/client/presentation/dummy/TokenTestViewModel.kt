package org.whosin.client.presentation.dummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.datastore.TokenManager

class TokenTestViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _refreshToken = MutableStateFlow<String?>(null)
    val refreshToken: StateFlow<String?> = _refreshToken.asStateFlow()

    init {
        loadTokens()
    }

    fun loadTokens() {
        viewModelScope.launch {
            _accessToken.value = tokenManager.getAccessToken()
            _refreshToken.value = tokenManager.getRefreshToken()
        }
    }

    fun saveTestTokens() {
        viewModelScope.launch {
            tokenManager.saveTokens(
                accessToken = "test_access_token",
                refreshToken = "test_refresh_token"
            )
            loadTokens()
        }
    }

    fun saveCustomTokens(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            tokenManager.saveTokens(accessToken, refreshToken)
            loadTokens()
        }
    }

    fun clearTokens() {
        viewModelScope.launch {
            tokenManager.clearToken()
            loadTokens()
        }
    }
}
