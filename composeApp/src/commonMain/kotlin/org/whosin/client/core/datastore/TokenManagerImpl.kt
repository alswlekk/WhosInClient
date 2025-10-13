package org.whosin.client.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.first
import org.whosin.client.core.network.invalidateAuthTokens

class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>
): TokenManager {
    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")
    private var httpClient: HttpClient? = null

    override suspend fun getAccessToken(): String? = dataStore.data.first()[accessKey]
    override suspend fun getRefreshToken(): String? = dataStore.data.first()[refreshKey]

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[accessKey] = accessToken
            it[refreshKey] = refreshToken
        }
        // 저장 후 즉시 확인하여 저장이 완료되었는지 검증
        val savedAccessToken = getAccessToken()
        val savedRefreshToken = getRefreshToken()
        println("저장된 토큰: AccessToken=$savedAccessToken, RefreshToken=$savedRefreshToken")
        
        if (savedAccessToken != accessToken || savedRefreshToken != refreshToken) {
            throw IllegalStateException("토큰 저장이 완료되지 않았습니다.")
        }
        
        // HttpClient의 캐시된 토큰 클리어하여 loadTokens가 다시 실행되도록 함
        httpClient?.invalidateAuthTokens()
    }

    override suspend fun clearToken() {
        dataStore.edit { it.clear() }
        println("토큰이 성공적으로 삭제되었습니다. ${getAccessToken()}, ${getRefreshToken()}")
        
        // HttpClient의 캐시된 토큰 클리어
        httpClient?.invalidateAuthTokens()
    }
    
    override fun setHttpClient(httpClient: HttpClient) {
        this.httpClient = httpClient
    }
}