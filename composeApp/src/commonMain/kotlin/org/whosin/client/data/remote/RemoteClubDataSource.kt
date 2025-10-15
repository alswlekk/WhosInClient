package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.AddClubResponseDto
import org.whosin.client.data.dto.response.ClubCodeConfirmResponseDto
import org.whosin.client.data.dto.response.ClubPresencesResponseDto
import org.whosin.client.data.dto.response.ErrorResponseDto
import org.whosin.client.data.dto.response.MyClubResponseDto

class RemoteClubDataSource(
    private val client: HttpClient
) {
    suspend fun getMyClubs(): ApiResult<MyClubResponseDto> {
        return try {
            val response: HttpResponse = client.get(urlString = "clubs/my")

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                ApiResult.Error(
                    code = response.status.value,
                    message = "HTTP Error: ${response.status.value}"
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun getPresentMembers(clubId: Int): ApiResult<ClubPresencesResponseDto> {
        return try {
            val response: HttpResponse = client.get(urlString = "clubs/$clubId/presences")

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                ApiResult.Error(
                    code = response.status.value,
                    message = "HTTP Error: ${response.status.value}"
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun checkIn(clubId: Int): ApiResult<Unit> {
        return try {
            val response: HttpResponse = client.post(urlString = "clubs/$clubId/check-in")

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                ApiResult.Error(
                    code = response.status.value,
                    message = "HTTP Error: ${response.status.value}"
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    suspend fun checkOut(clubId: Int): ApiResult<Unit> {
        return try {
            val response: HttpResponse = client.delete(urlString = "clubs/$clubId/check-out")

            if (response.status.isSuccess()) {
                ApiResult.Success(
                    data = response.body(),
                    statusCode = response.status.value
                )
            } else {
                ApiResult.Error(
                    code = response.status.value,
                    message = "HTTP Error: ${response.status.value}"
                )
            }
        } catch (t: Throwable) {
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    // 동아리 번호 확인
    suspend fun confirmClubCode(clubCode: String): ApiResult<ClubCodeConfirmResponseDto>{
        return try {
            val response: HttpResponse = client.get(urlString = "clubs"){
                parameter("clubNumber", clubCode)
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
        } catch (t: Throwable){
            ApiResult.Error(message = t.message, cause = t)
        }
    }

    // 동아리 추가 함수
    suspend fun addClub(clubId: Int): ApiResult<AddClubResponseDto> {
        return try {
            val response: HttpResponse = client.post(urlString = "clubs/$clubId")

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
        } catch (t: Throwable){
            ApiResult.Error(message = t.message, cause = t)
        }
    }
}