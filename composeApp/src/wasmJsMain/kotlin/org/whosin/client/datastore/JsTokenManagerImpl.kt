package org.whosin.client.datastore

import org.whosin.client.core.datastore.TokenManager

class JsTokenManagerImpl : TokenManager{
    override suspend fun getAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getRefreshToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearToken() {
        TODO("Not yet implemented")
    }
}