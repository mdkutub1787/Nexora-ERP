package com.kutub.nexora.erp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun login(email: String, name: String = "Md Kutub Uddin", phone: String = "+8801700000000") {
        viewModelScope.launch {
            preferencesManager.setLoggedIn(true)
            preferencesManager.setUserProfile(name = name, email = email, phone = phone)
        }
    }
}
