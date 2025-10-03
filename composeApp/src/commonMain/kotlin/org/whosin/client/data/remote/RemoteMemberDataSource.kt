package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.request.EmailValidationRequestDto
import org.whosin.client.data.dto.request.EmailVerificationRequestDto
import org.whosin.client.data.dto.request.LoginRequestDto
import org.whosin.client.data.dto.request.SignupRequestDto
import org.whosin.client.data.dto.response.EmailVerificationResponseDto
import org.whosin.client.data.dto.response.LoginResponseDto
import org.whosin.client.data.dto.response.SignupResponseDto

class RemoteMemberDataSource(
    private val client: HttpClient
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> {
        return try {
            val response: HttpResponse = client
                .post("api/auth/login") {
                    setBody(
                        LoginRequestDto(email = email, password = password)
                    )
                }
            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                val errorResponse: LoginResponseDto = response.body()
                ApiResult.Error(
                    code = errorResponse.status,
                    message = errorResponse.message
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun sendEmailVerification(email: String): ApiResult<EmailVerificationResponseDto> {
        return try {
            val response: HttpResponse = client
                .post("api/auth/email/send") {
                    setBody(
                        EmailVerificationRequestDto(email = email)
                    )
                }
            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                val errorResponse: EmailVerificationResponseDto = response.body()
                ApiResult.Error(
                    code = errorResponse.status,
                    message = errorResponse.message
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun validateEmailCode(
        email: String,
        authCode: String
    ): ApiResult<EmailVerificationResponseDto> {
        return try {
            val response: HttpResponse = client
                .post("api/auth/email/validation") {
                    setBody(
                        EmailValidationRequestDto(email = email, authCode = authCode)
                    )
                }
            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                val errorResponse: EmailVerificationResponseDto = response.body()
                ApiResult.Error(
                    code = errorResponse.status,
                    message = errorResponse.message
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun signup(
        email: String,
        password: String,
        nickName: String
    ): ApiResult<SignupResponseDto> {
        return try {
            val response: HttpResponse = client
                .post("api/users/signup") {
                    setBody(
                        SignupRequestDto(email = email, password = password, nickName = nickName)
                    )
                }
            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                val errorResponse: SignupResponseDto = response.body()
                ApiResult.Error(
                    code = errorResponse.status,
                    message = errorResponse.message
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }
}