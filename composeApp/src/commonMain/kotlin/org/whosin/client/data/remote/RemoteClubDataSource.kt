package org.whosin.client.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.whosin.client.core.network.ApiResult
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
}