package org.whosin.client.presentation.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.MemberRepository

sealed interface SignupUiState {
    data object Idle: SignupUiState
    data object Loading: SignupUiState
    data object Success: SignupUiState
    data class Error(val message: String?): SignupUiState
}

class SignupViewModel(
    private val repository: MemberRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<SignupUiState> = MutableStateFlow(SignupUiState.Idle)
    val uiState: StateFlow<SignupUiState> = _uiState

    fun signup(email: String, password: String, nickName: String) {
        _uiState.value = SignupUiState.Loading
        viewModelScope.launch {
            when (val result = repository.signup(email, password, nickName)) {
                is ApiResult.Success -> {
                    _uiState.value = SignupUiState.Success
                }
                is ApiResult.Error -> {
                    val message = result.message ?: result.cause?.message
                    _uiState.value = SignupUiState.Error(message)
                }
            }
        }
    }
}