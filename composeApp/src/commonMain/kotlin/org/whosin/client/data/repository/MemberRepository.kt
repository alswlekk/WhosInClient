package org.whosin.client.data.repository

import org.whosin.client.data.remote.RemoteMemberDataSource
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.LoginResponseDto

class MemberRepository(
    private val dataSource: RemoteMemberDataSource
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> =
        dataSource.login(email, password)

}