package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.request.LoginRequestDto
import org.whosin.client.data.dto.request.EmailVerificationRequestDto
import org.whosin.client.data.dto.response.LoginResponseDto
import org.whosin.client.data.dto.response.EmailVerificationResponseDto

class RemoteMemberDataSource(
    private val client: HttpClient
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> {
        return try {
            val response: HttpResponse = client
                .post("api/members/login") {
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
                ApiResult.Error(
                    code = response.status.value,
                    message = "HTTP ${response.status.value}"
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
}