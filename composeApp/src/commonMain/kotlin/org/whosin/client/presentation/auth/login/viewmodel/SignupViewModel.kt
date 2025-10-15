package org.whosin.client.presentation.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.AuthRepository

sealed interface SignupUiState {
    data object Idle: SignupUiState
    data object Loading: SignupUiState
    data object Success: SignupUiState
    data class Error(val message: String?): SignupUiState
}

class SignupViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _uiState: MutableStateFlow<SignupUiState> = MutableStateFlow(SignupUiState.Idle)
    val uiState: StateFlow<SignupUiState> = _uiState

    fun signup(email: String, password: String, nickName: String) {
        _uiState.value = SignupUiState.Loading
        viewModelScope.launch {
            when (val signupResult = repository.signup(email, password, nickName)) {
                is ApiResult.Success -> {
                    // 회원가입 성공 시 자동 로그인
                    when (val loginResult = repository.login(email, password)) {
                        is ApiResult.Success -> {
                            val tokenData = loginResult.data.data
                            if (tokenData != null) {
                                tokenManager.saveTokens(
                                    accessToken = tokenData.accessToken,
                                    refreshToken = tokenData.refreshToken
                                )
                                _uiState.value = SignupUiState.Success
                            } else {
                                _uiState.value = SignupUiState.Error("로그인 후 토큰 데이터를 받지 못했습니다.")
                            }
                        }
                        is ApiResult.Error -> {
                            val message = loginResult.message ?: loginResult.cause?.message
                            _uiState.value = SignupUiState.Error(message)
                        }
                    }
                }
                is ApiResult.Error -> {
                    val message = signupResult.message ?: signupResult.cause?.message
                    _uiState.value = SignupUiState.Error(message)
                }
            }
        }
    }
}