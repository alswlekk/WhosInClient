package org.whosin.client.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.whosin.client.core.auth.TokenExpiredManager
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.ClubData
import org.whosin.client.data.repository.AuthRepository
import org.whosin.client.data.repository.MemberRepository

data class MyPageUiState(
    val isLoading: Boolean = false,
    val isEditable: Boolean = false,
    val nickname: String = "",
    val clubs: List<ClubData> = emptyList(),
    val errorMessage: String? = null,
)

class MyPageViewModel(
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
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
            when (val result = memberRepository.getMyInfo()) {
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

    // 내 정보 수정
    fun updateMyInfo(newNickName: String, clubList: List<ClubData>?) {
        viewModelScope.launch {
            _uiState.update{ it.copy(isLoading = true) }
            val newClubs = clubList?.map {
                it.clubId
            }
            when (val result = memberRepository.updateMyInfo(newNickName = newNickName, clubList = newClubs)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(isEditable = false)
                    }
                    getMyInfo()
                    println("MyPageViewModel: 내 정보 수정 성공")
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "내 정보 수정에 실패했습니다."
                    )
                    println("MyPageViewModel: 내 정보 수정 실패 - ${result.message}")
                }
            }
        }
    }

    // 클럽 삭제 (UI 상태에서만 제거)
    fun deleteClub(clubId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                clubs = currentState.clubs.filter { it.clubId != clubId }
            )
        }
        println("MyPageViewModel: 클럽 삭제 - clubId: $clubId")
    }

    // 로그아웃
    fun logout(){
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken()
            
            // 서버에 로그아웃 요청 (실패해도 상관없음)
            if (!refreshToken.isNullOrEmpty()) {
                try {
                    authRepository.logout(refreshToken)
                    println("MyPageViewModel: 로그아웃 API 호출 완료")
                } catch (e: Exception) {
                    println("MyPageViewModel: 로그아웃 API 호출 실패 - ${e.message}")
                }
            }
            
            // 토큰 삭제 및 로그인 화면으로 이동
            tokenManager.clearToken()
            TokenExpiredManager.setTokenExpired()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update{ it.copy(isLoading = true) }
            when (val result = authRepository.deleteAccount()) {
                is ApiResult.Success -> {
                    println("MyPageViewModel: 회원 탈퇴 성공")
                    // 토큰 삭제 및 로그인 화면으로 이동
                    tokenManager.clearToken()
                    TokenExpiredManager.setTokenExpired()
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "회원 탈퇴에 실패했습니다."
                    )
                    println("MyPageViewModel: 회원 탈퇴 실패 - ${result.message}")
                }
            }
        }
    }
}