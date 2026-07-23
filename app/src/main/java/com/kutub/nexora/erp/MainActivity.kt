package com.kutub.nexora.erp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.kutub.nexora.erp.data.local.PreferencesManager
import com.kutub.nexora.erp.ui.navigation.AppNavGraph
import com.kutub.nexora.erp.ui.theme.NexoraERPTheme
import com.kutub.nexora.erp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kutub.nexora.erp.utils.BiometricHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    @OptIn(androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupContent() {
        setContent {
            val themeMode by preferencesManager.themeModeFlow.collectAsState(initial = "system")
            val windowSizeClass = calculateWindowSizeClass(this)
            
            NexoraERPTheme(
                themeMode = themeMode,
                windowWidthSizeClass = windowSizeClass.widthSizeClass
            ) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
