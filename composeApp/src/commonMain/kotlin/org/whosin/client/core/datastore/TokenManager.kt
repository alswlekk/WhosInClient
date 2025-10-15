package org.whosin.client.core.datastore

import io.ktor.client.HttpClient

interface TokenManager {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearToken()
    
    // HttpClient 참조 설정 (토큰 변경 시 자동 클리어용)
    fun setHttpClient(httpClient: HttpClient)
}