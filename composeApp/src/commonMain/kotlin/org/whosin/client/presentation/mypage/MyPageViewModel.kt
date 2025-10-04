package org.whosin.client.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.ClubData
import org.whosin.client.data.repository.MemberRepository

data class MyPageUiState(
    val isLoading: Boolean = false,
    val isEditable: Boolean = false,
    val nickname: String = "",
    val clubs: List<ClubData> = emptyList(),
    val errorMessage: String? = null,
)

class MyPageViewModel(
    private val repository: MemberRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        getMyInfo()
    }

    // 수정 모드 변경
    fun toggleEditMode() {
        _uiState.update { it.copy(isEditable = !it.isEditable) }
    }

    // 닉네임 변경
    fun updateNickName(newNickname: String) {
        _uiState.update { it.copy(nickname = newNickname) }
    }

    // 내 정보 조회
    fun getMyInfo() {
        viewModelScope.launch {
            _uiState.update{ it.copy(isLoading = true) }
            when (val result = repository.getMyInfo()) {
                is ApiResult.Success -> {
                    val response = result.data.data
                    _uiState.update { it ->
                        it.copy(
                            isLoading = false,
                            nickname = response.nickName,
                            clubs = response.clubList.map { clubData ->
                                ClubData(
                                    clubId = clubData.id,
                                    clubName = clubData.name
                                )
                            },
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

    // 클럽 삭제 (UI 상태에서만 제거)
    fun deleteClub(clubId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                clubs = currentState.clubs.filter { it.clubId != clubId }
            )
        }
        println("MyPageViewModel: 클럽 삭제 - clubId: $clubId")
    }

}