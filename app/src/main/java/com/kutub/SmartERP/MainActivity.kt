package com.kutub.smarterp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.kutub.smarterp.data.local.PreferencesManager
import com.kutub.smarterp.ui.navigation.AppNavGraph
import com.kutub.smarterp.ui.theme.SmartERPTheme
import com.kutub.smarterp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kutub.smarterp.utils.BiometricHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.activity.enableEdgeToEdge

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
            val themeColor by preferencesManager.themeColorFlow.collectAsState(initial = "#6366F1")
            val windowSizeClass = calculateWindowSizeClass(this)
            
            SmartERPTheme(
                themeMode = themeMode,
                primaryColorHex = themeColor,
                windowWidthSizeClass = windowSizeClass.widthSizeClass
            ) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}



