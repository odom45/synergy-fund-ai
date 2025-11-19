package com.synergyfund.ai.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

object SessionManager {
    private const val TOKEN_KEY_NAME = "auth_token"
    private val TOKEN_KEY = stringPreferencesKey(TOKEN_KEY_NAME)

    @Volatile
    private var cachedToken: String? = null

    private var appContext: Context? = null

    fun init(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
            // Preload token from DataStore (non-blocking)
            CoroutineScope(Dispatchers.IO).launch {
                cachedToken = appContext!!.sessionDataStore.data
                    .map { prefs -> prefs[TOKEN_KEY] }
                    .first()
            }
        }
    }

    fun getToken(): String? = cachedToken

    suspend fun setToken(token: String) {
        cachedToken = token
        appContext?.sessionDataStore?.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun clear() {
        cachedToken = null
        appContext?.sessionDataStore?.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}
