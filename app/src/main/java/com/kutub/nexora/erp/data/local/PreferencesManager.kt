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

        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
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

    val isLoggedInFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userNameFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Md Kutub Uddin" // default placeholder
    }

    val userEmailFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: "mdkutub150@gmail.com"
    }

    val userPhoneFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_PHONE] ?: "+8801700000000"
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

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences -> preferences[IS_LOGGED_IN] = isLoggedIn }
    }

    suspend fun setUserProfile(name: String, email: String, phone: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            preferences[USER_PHONE] = phone
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_PHONE)
        }
    }
}
