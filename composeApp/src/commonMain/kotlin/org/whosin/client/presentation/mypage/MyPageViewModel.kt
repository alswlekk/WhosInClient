package org.whosin.client.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.MyClubData
import org.whosin.client.data.repository.MemberRepository

data class MyPageUiState(
    val isLoading: Boolean = false,
    val isEditable: Boolean = false,
    val nickname: String = "",
    val clubs: List<MyClubData> = emptyList(),
    val errorMessage: String? = null,
)

class MyPageViewModel(
    private val repository: MemberRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    // 수정 모드 변경
    fun enableEditMode() {
        _uiState.update { it.copy(isEditable = !it.isEditable) }
    }

    // 내 정보 조회
    fun getMyInfo() {
        viewModelScope.launch {
            _uiState.update{ it.copy(isLoading = true) }
            when (val result = repository.getMyInfo()) {
                is ApiResult.Success -> {
                    val response = result.data.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nickname = response.nickname,
                            clubs = response.clubList,
                            errorMessage = null
                        )
                    }
                    println("MyPageViewModel: 내 정보 조회 성공")
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "내 정보 조회에 실패했습니다."
                    )
                    println("MyPageViewModel: 내 정보 조회 실패 - ${result.message}")
                }
            }
        }
    }

    // TODO: 내 정보 수정


}