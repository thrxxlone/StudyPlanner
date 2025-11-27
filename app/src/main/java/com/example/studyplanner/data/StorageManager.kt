package com.example.studyplanner.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")

class StorageManager(private val context: Context) {

    companion object {
        val USER_UID = stringPreferencesKey("user_uid")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    //  Зберегти UID та email
    suspend fun saveUser(uid: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_UID] = uid
            prefs[USER_EMAIL] = email
            prefs[IS_LOGGED_IN] = true
        }
    }

    // 🔹 Зберегти імʼя
    suspend fun saveName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
        }
    }

    // 🔹 Отримати імʼя
    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }

    // 🔹 Отримати UID
    val userUid: Flow<String?> = context.dataStore.data.map { it[USER_UID] }

    // 🔹 Отримати email
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }

    // 🔹 Авторизований
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }

    // 🔹 Видалити всі дані
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
