package org.whosin.client.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    /* 로그인, 회원가입 등 인증 관련*/
    @Serializable
    data object AuthGraph: Route

    @Serializable
    data object Splash: Route

    @Serializable
    data object Login: Route

    @Serializable
    data object FindPassword: Route

    @Serializable
    data object Signup: Route

    @Serializable
    data class EmailVerification(val email: String): Route

    @Serializable
    data class PasswordInput(val email: String): Route

    @Serializable
    data class NicknameInput(val email: String, val password: String): Route

    @Serializable
    data object ClubCodeInput: Route

    /* 메인 화면 */
    @Serializable
    data object Home: Route

    /* 마이 페이지 */
    @Serializable
    data object MyPage: Route

    /* 내 정보 수정하기 */
    @Serializable
    data object UpdateMyInfo: Route
}
