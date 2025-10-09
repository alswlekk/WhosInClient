package org.whosin.client.presentation.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.AuthRepository

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.login(email, password)) {
                is ApiResult.Success -> {
                    val tokenData = result.data.data
                    if (tokenData != null) {
                        tokenManager.saveTokens(
                            accessToken = tokenData.accessToken,
                            refreshToken = tokenData.refreshToken
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "토큰 데이터를 받지 못했습니다."
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "로그인에 실패했습니다."
                    )
                }
            }
        }
    }
}