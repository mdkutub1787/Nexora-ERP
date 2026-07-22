package com.kutub.nexora.erp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode") // "system", "dark", "light"
        val LANGUAGE = stringPreferencesKey("language") // "en", "bn"
        val CURRENCY = stringPreferencesKey("currency") // "$", "৳"
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }

    val themeModeFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "system"
    }

    val languageFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "en"
    }

    val currencyFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENCY] ?: "$"
    }

    val biometricEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED] ?: false
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences -> preferences[THEME_MODE] = mode }
    }

    suspend fun setLanguage(lang: String) {
        dataStore.edit { preferences -> preferences[LANGUAGE] = lang }
    }

    suspend fun setCurrency(currency: String) {
        dataStore.edit { preferences -> preferences[CURRENCY] = currency }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[BIOMETRIC_ENABLED] = enabled }
    }
}
