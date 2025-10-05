package org.whosin.client.data.repository

import org.whosin.client.data.remote.RemoteMemberDataSource
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.LoginResponseDto
import org.whosin.client.data.dto.response.MyInfoResponseDto
import org.whosin.client.data.dto.response.UpdateMyInfoResponseDto

class MemberRepository(
    private val dataSource: RemoteMemberDataSource
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> =
        dataSource.login(email, password)

    suspend fun getMyInfo(): ApiResult<MyInfoResponseDto> =
        dataSource.getMyInfo()

    suspend fun updateMyInfo(newNickName: String, clubList: List<Int>?): ApiResult<UpdateMyInfoResponseDto> =
        dataSource.updateMyInfo(newNickName = newNickName, clubList = clubList)

}