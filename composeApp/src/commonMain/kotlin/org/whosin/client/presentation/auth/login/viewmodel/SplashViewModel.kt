package org.whosin.client.presentation.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.datastore.TokenManager

data class SplashUiState(
    val isLoading: Boolean = true,
    val hasToken: Boolean = false
)

class SplashViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    fun checkToken() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val hasValidToken = !accessToken.isNullOrEmpty()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasToken = hasValidToken
            )
        }
    }
}
