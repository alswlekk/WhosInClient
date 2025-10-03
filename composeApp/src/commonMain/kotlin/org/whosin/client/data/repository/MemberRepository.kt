package org.whosin.client.data.repository

import org.whosin.client.data.remote.RemoteMemberDataSource
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.LoginResponseDto
import org.whosin.client.data.dto.response.EmailVerificationResponseDto
import org.whosin.client.data.dto.response.SignupResponseDto

class MemberRepository(
    private val dataSource: RemoteMemberDataSource
) {
    suspend fun login(
        email: String,
        password: String
    ): ApiResult<LoginResponseDto> =
        dataSource.login(email, password)

    suspend fun sendEmailVerification(
        email: String
    ): ApiResult<EmailVerificationResponseDto> =
        dataSource.sendEmailVerification(email)

    suspend fun validateEmailCode(
        email: String,
        authCode: String
    ): ApiResult<EmailVerificationResponseDto> =
        dataSource.validateEmailCode(email, authCode)

    suspend fun signup(
        email: String,
        password: String,
        nickName: String
    ): ApiResult<SignupResponseDto> =
        dataSource.signup(email, password, nickName)

}