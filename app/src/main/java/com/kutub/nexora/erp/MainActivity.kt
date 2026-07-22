package com.kutub.nexora.erp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.kutub.nexora.erp.data.local.PreferencesManager
import com.kutub.nexora.erp.ui.navigation.AppNavGraph
import com.kutub.nexora.erp.ui.theme.NexoraERPTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by preferencesManager.themeModeFlow.collectAsState(initial = "system")
            
            NexoraERPTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
