package com.example.zoroastervers.data.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    override suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    override suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { settings ->
            settings[ACCESS_TOKEN_KEY] = token
        }
    }

    override suspend fun deleteAccessToken() {
        context.dataStore.edit { settings ->
            settings.remove(ACCESS_TOKEN_KEY)
        }
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { settings ->
            settings[REFRESH_TOKEN_KEY] = token
        }
    }

    override suspend fun deleteRefreshToken() {
        context.dataStore.edit { settings ->
            settings.remove(REFRESH_TOKEN_KEY)
        }
    }
}