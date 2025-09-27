package org.whosin.client.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>
): TokenManager {
    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")

    override suspend fun getAccessToken(): String? = dataStore.data.first()[accessKey]
    override suspend fun getRefreshToken(): String? = dataStore.data.first()[refreshKey]

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[accessKey] = accessToken
            it[refreshKey] = refreshToken
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.clear() }
    }
}