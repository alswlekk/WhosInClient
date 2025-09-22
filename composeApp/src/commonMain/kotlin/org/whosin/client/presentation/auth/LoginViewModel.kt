package org.whosin.client.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.MemberRepository
import org.whosin.client.data.dto.response.TokenDto

sealed interface LoginUiState {
    data object Loading: LoginUiState
    data class Success(val token: TokenDto): LoginUiState
    data class Error(val message: String?): LoginUiState
}

class LoginViewModel(
    private val repository: MemberRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<LoginUiState?> = MutableStateFlow(null)
    val uiState: StateFlow<LoginUiState?> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            when (val result = repository.login(email, password)) {
                is ApiResult.Success -> {
                    _uiState.value = LoginUiState.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    val message = result.message ?: result.cause?.message
                    _uiState.value = LoginUiState.Error(message)
                }
            }
        }
    }
}