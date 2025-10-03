package org.whosin.client.presentation.auth.clubcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.ClubRepository

data class AddClubUiState(
    val isLoading: Boolean = false,
    val verificationState: ClubCodeState = ClubCodeState.INPUT,
    val clubName: String? = null,
    val clubId: Int? = null,
    val errorMessage: String? = null
)

class AddClubViewModel(
    private val repository: ClubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddClubUiState())
    val uiState: StateFlow<AddClubUiState> = _uiState.asStateFlow()

    fun confirmClubCode(clubCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.confirmClubCode(clubCode = clubCode)) {
                is ApiResult.Success -> {
                    val response = result.data.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationState = ClubCodeState.SUCCESS,
                            clubName = response.clubName,
                            clubId = response.clubId,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationState = ClubCodeState.ERROR,
                            errorMessage = result.message ?: "동아리 이름 조회에 오류가 발생했습니다."
                        )
                    }
                }
            }
        }
    }

    // 에러 상태 리셋 함수
    fun resetErrorState() {
        _uiState.update {
            it.copy(
                verificationState = ClubCodeState.INPUT,
                errorMessage = null,
                clubName = null,
                clubId = null
            )
        }
    }

    // 동아리 추가 함수
    fun addClub(clubId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.addClub(clubId = clubId)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "동아리 추가에 오류가 발생했습니다."
                        )
                    }
                }
            }
        }
    }
}