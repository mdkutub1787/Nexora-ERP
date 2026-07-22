package com.kutub.nexora.erp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.kutub.nexora.erp.data.local.PreferencesManager
import com.kutub.nexora.erp.ui.navigation.AppNavGraph
import com.kutub.nexora.erp.ui.theme.NexoraERPTheme
import com.kutub.nexora.erp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.kutub.nexora.erp.utils.BiometricHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            // Apply language early
            val lang = preferencesManager.languageFlow.first()
            LocaleHelper.applyLocale(lang)

            val isBiometricEnabled = preferencesManager.biometricEnabledFlow.first()
            if (isBiometricEnabled && BiometricHelper.isBiometricAvailable(this@MainActivity)) {
                BiometricHelper.showBiometricPrompt(
                    activity = this@MainActivity,
                    onSuccess = { setupContent() },
                    onError = { finish() }
                )
            } else {
                setupContent()
            }
        }
    }

    private fun setupContent() {
        setContent {
            val themeMode by preferencesManager.themeModeFlow.collectAsState(initial = "system")
            
            NexoraERPTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
