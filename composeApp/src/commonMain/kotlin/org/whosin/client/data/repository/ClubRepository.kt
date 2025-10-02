package org.whosin.client.data.repository

import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.ClubPresencesResponseDto
import org.whosin.client.data.dto.response.MyClubResponseDto
import org.whosin.client.data.remote.RemoteClubDataSource

class ClubRepository(
    private val dataSource: RemoteClubDataSource
) {
    suspend fun getMyClubs(): ApiResult<MyClubResponseDto> =
        dataSource.getMyClubs()

    suspend fun getPresentMembers(clubId: Int): ApiResult<ClubPresencesResponseDto> =
        dataSource.getPresentMembers(clubId)

    suspend fun checkIn(clubId: Int): ApiResult<Unit> =
        dataSource.checkIn(clubId)

    suspend fun checkOut(clubId: Int): ApiResult<Unit> =
        dataSource.checkOut(clubId)
}