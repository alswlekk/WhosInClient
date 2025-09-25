package org.whosin.client.presentation.auth.clubcode

enum class ClubCodeState {
    INPUT,      // 입력 중
    SUCCESS,    // 성공 (동아리 정보 표시)
    ERROR       // 실패 (에러 메시지 표시)
}