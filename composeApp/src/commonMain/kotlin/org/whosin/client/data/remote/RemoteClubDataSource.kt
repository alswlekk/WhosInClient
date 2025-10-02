package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.ClubPresencesResponseDto
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
}