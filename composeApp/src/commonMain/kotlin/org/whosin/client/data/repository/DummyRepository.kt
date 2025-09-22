package org.whosin.client.data.repository

import org.whosin.client.data.remote.DummyDataSource
import org.whosin.client.core.network.ApiResult
import org.whosin.client.data.dto.response.DummyJokeResponseDto

class DummyRepository(
    private val dataSource: DummyDataSource
) {
    suspend fun getRandomJoke(): ApiResult<DummyJokeResponseDto> = dataSource.getRandomJoke()
}