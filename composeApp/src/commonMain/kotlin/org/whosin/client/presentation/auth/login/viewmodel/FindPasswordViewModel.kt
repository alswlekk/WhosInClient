package org.whosin.client.presentation.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.MemberRepository

sealed interface FindPasswordUiState {
    data object Idle: FindPasswordUiState
    data object Loading: FindPasswordUiState
    data object Success: FindPasswordUiState
    data class Error(val message: String?): FindPasswordUiState
}

class FindPasswordViewModel(
    private val repository: MemberRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<FindPasswordUiState> = MutableStateFlow(FindPasswordUiState.Idle)
    val uiState: StateFlow<FindPasswordUiState> = _uiState

    fun sendPasswordResetEmail(email: String) {
        _uiState.value = FindPasswordUiState.Loading
        viewModelScope.launch {
            when (val result = repository.sendPasswordResetEmail(email)) {
                is ApiResult.Success -> {
                    _uiState.value = FindPasswordUiState.Success
                }
                is ApiResult.Error -> {
                    val message = result.message ?: result.cause?.message
                    _uiState.value = FindPasswordUiState.Error(message)
                }
            }
        }
    }
}