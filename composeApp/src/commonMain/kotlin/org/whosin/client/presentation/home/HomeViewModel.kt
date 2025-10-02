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

data class HomeUiState(
    val isClubsLoading: Boolean = true,
    val isMembersLoading: Boolean = false,
    val clubs: List<ClubUi> = emptyList(),
    val selectedClub: ClubUi? = null,
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
        _uiState.update { it.copy(selectedClub = club) }
//        loadPresentMembers(club.id)
    }
}