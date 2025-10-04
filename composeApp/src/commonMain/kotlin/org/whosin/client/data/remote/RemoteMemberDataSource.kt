package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.request.LoginRequestDto
import org.whosin.client.data.dto.request.UpdateMyInfoRequestDto
import org.whosin.client.data.dto.response.ErrorResponseDto
import org.whosin.client.data.dto.response.LoginResponseDto
import org.whosin.client.data.dto.response.MyInfoResponseDto
import org.whosin.client.data.dto.response.UpdateMyInfoResponseDto

class RemoteMemberDataSource(
    private val client: HttpClient
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> {
        return try {
            val response: HttpResponse = client
                // TODO: BaseUrl 가져올 수 있도록 처리
                .post(urlString = "BASEURL/members/login") {
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

    // 내 정보 조회
    suspend fun getMyInfo(): ApiResult<MyInfoResponseDto> {
        return try {
            val response: HttpResponse = client.get(urlString = "users/myPage")

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                // 에러 응답 파싱 시도
                try {
                    val errorResponse: ErrorResponseDto = response.body()
                    ApiResult.Error(
                        code = response.status.value,
                        message = errorResponse.message
                    )
                } catch (e: Exception) {
                    // 파싱 실패 시 기본 에러 메시지
                    ApiResult.Error(
                        code = response.status.value,
                        message = "HTTP Error: ${response.status.value}"
                    )
                }
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    // 내 정보 수정
    suspend fun updateMyInfo(
        newNickName: String,
        clubList: List<Int>?
    ): ApiResult<UpdateMyInfoResponseDto> {
        return try {
            val response: HttpResponse = client.patch(urlString = "users/myPage/update"){
                setBody{
                    UpdateMyInfoRequestDto(nickName = newNickName, clubList = clubList)
                }
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                // 에러 응답 파싱 시도
                try {
                    val errorResponse: ErrorResponseDto = response.body()
                    ApiResult.Error(
                        code = response.status.value,
                        message = errorResponse.message
                    )
                } catch (e: Exception) {
                    // 파싱 실패 시 기본 에러 메시지
                    ApiResult.Error(
                        code = response.status.value,
                        message = "HTTP Error: ${response.status.value}"
                    )
                }
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }
}