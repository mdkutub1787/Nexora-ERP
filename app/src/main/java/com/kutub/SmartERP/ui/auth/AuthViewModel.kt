package com.kutub.smarterp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.smarterp.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun login(email: String) {
        viewModelScope.launch {
            preferencesManager.setLoggedIn(true)
            // Note: We don't overwrite the profile here so the user's saved data persists
        }
    }
}


