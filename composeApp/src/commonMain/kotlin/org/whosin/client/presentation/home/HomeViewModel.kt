package org.whosin.client.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.repository.ClubRepository

data class ClubUi(
    val id: Int,
    val name: String
)

data class PresentMemberUi(
    val userName: String,
    val isMe: Boolean
)

data class HomeUiState(
    val isClubsLoading: Boolean = true,
    val isMembersLoading: Boolean = false,
    val isToggleLoading: Boolean = false,
    val clubs: List<ClubUi> = emptyList(),
    val selectedClub: ClubUi? = null,
    val selectedClubName: String? = null,
    val presentMembers: List<PresentMemberUi> = emptyList(),
    val isAttending: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val clubRepository: ClubRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMyClubs()
    }

    private fun loadMyClubs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClubsLoading = true) }
            when (val result = clubRepository.getMyClubs()) {
                is ApiResult.Success -> {
                    val clubs = result.data.data.userClubs.map { ClubUi(it.clubId, it.clubName) }
                    _uiState.update { it.copy(isClubsLoading = false, clubs = clubs) }
                    // 첫 번째 클럽을 자동으로 선택하고 멤버 목록 로드
                    clubs.firstOrNull()?.let { firstClub ->
                        onClubSelected(firstClub)
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isClubsLoading = false, errorMessage = "동아리 목록을 불러오지 못했습니다.") }
                }
            }
        }
    }

    fun onClubSelected(club: ClubUi) {
        _uiState.update { it.copy(selectedClub = club, presentMembers = emptyList()) } // 클럽 변경 시 멤버 목록 초기화
        loadPresentMembers(club.id)
    }

    fun refresh() {
        _uiState.value.selectedClub?.id?.let {
            loadPresentMembers(it)
        }
    }

    private fun loadPresentMembers(clubId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isMembersLoading = true, errorMessage = null) }

            when (val result = clubRepository.getPresentMembers(clubId)) {
                is ApiResult.Success -> {
                    val responseData = result.data.data
                    val membersUi = responseData.presentMembers.map { dto ->
                        PresentMemberUi(userName = dto.userName, isMe = dto.isMe)
                    }

                    val amIAttending = membersUi.any { it.isMe }

                    _uiState.update {
                        it.copy(
                            isMembersLoading = false,
                            selectedClubName = responseData.clubName,
                            presentMembers = membersUi,
                            isAttending = amIAttending
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isMembersLoading = false,
                            errorMessage = result.message ?: "재실자 명단을 불러오지 못했습니다."
                        )
                    }
                }
            }
        }
    }

    fun toggleAttendance() {
        val currentState = _uiState.value
        // 클럽이 선택되지 않았거나, 이미 출석/퇴실 요청이 진행 중이면 아무것도 하지 않음
        val clubId = currentState.selectedClub?.id ?: return
        if (currentState.isToggleLoading) return

        viewModelScope.launch {
            // 버튼 로딩 상태 시작
            _uiState.update { it.copy(isToggleLoading = true, errorMessage = null) }

            // 현재 출석 상태에 따라 checkOut 또는 checkIn 호출
            val result = if (currentState.isAttending) {
                clubRepository.checkOut(clubId)
            } else {
                clubRepository.checkIn(clubId)
            }

            when (result) {
                is ApiResult.Success -> {
                    // 성공 시, 재실자 목록을 새로고침하여 최신 상태를 반영
                    loadPresentMembers(clubId)
                }
                is ApiResult.Error -> {
                    // 실패 시, 에러 메시지 표시
                    _uiState.update { it.copy(errorMessage = result.message ?: "요청에 실패했습니다.") }
                }
            }

            // 버튼 로딩 상태 종료
            _uiState.update { it.copy(isToggleLoading = false) }
        }
    }
}