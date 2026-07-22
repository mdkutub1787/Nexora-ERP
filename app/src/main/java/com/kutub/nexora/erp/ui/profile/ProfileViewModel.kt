package com.kutub.nexora.erp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userName: StateFlow<String> = preferencesManager.userNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val userEmail: StateFlow<String> = preferencesManager.userEmailFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val userPhone: StateFlow<String> = preferencesManager.userPhoneFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            preferencesManager.setUserProfile(name, email, phone)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.setLoggedIn(false)
        }
    }
}
