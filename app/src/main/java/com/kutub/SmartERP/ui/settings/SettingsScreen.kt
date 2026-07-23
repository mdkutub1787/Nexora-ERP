package com.kutub.smarterp.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.smarterp.utils.BiometricHelper
import com.kutub.smarterp.utils.CurrencyUtils
import com.kutub.smarterp.utils.LocaleHelper
import com.kutub.smarterp.ui.components.SmartERPGlobalDialog

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState()
    val currentTheme by viewModel.themeMode.collectAsState()
    val currentCurrency by viewModel.currentCurrency.collectAsState()
    val currentLanguage by viewModel.language.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val dimens = com.kutub.smarterp.ui.theme.LocalDimens.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimens.paddingLarge)
        ) {
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = dimens.paddingMedium)
            )

            SettingsToggleItem(
                title = "Biometric Lock",
                subtitle = "Use fingerprint to unlock the app",
                icon = Icons.Default.Fingerprint,
                checked = isBiometricEnabled,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        if (BiometricHelper.isBiometricAvailable(context)) {
                            // Professional Verification before enabling
                            (context as? androidx.fragment.app.FragmentActivity)?.let { activity ->
                                BiometricHelper.showBiometricPrompt(
                                    activity = activity,
                                    onSuccess = { viewModel.setBiometricEnabled(true) },
                                    onError = { 
                                        Toast.makeText(context, "Authentication failed. Could not enable biometric.", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        } else {
                            Toast.makeText(context, "Biometric setup is not available on this device.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        viewModel.setBiometricEnabled(false)
                    }
                }
            )

            Spacer(Modifier.height(dimens.paddingExtraLarge))
            
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = dimens.paddingMedium)
            )

            SettingsCurrencyItem(
                title = "Currency",
                subtitle = "Select default currency for products",
                icon = Icons.Default.AttachMoney,
                currentCurrency = currentCurrency,
                onCurrencyChange = { viewModel.setCurrency(it) }
            )

            Spacer(Modifier.height(dimens.paddingLarge))

            SettingsToggleItem(
                title = "Dark Mode",
                subtitle = "Toggle dark/light theme",
                icon = Icons.Default.DarkMode,
                checked = currentTheme == "dark",
                onCheckedChange = { isDark -> viewModel.setThemeMode(if (isDark) "dark" else "light") }
            )

            Spacer(Modifier.height(dimens.paddingLarge))

            SettingsToggleItem(
                title = "Bangla Language",
                subtitle = "Switch app language to Bengali",
                icon = Icons.Default.Language,
                checked = currentLanguage == "bn",
                onCheckedChange = { isBn -> 
                    val newLang = if (isBn) "bn" else "en"
                    viewModel.setLanguage(newLang)
                    LocaleHelper.applyLocale(newLang)
                }
            )

            Spacer(Modifier.height(dimens.paddingLarge))

            ThemeColorPickerItem(
                onColorSelect = { viewModel.setThemeColor(it) }
            )

            Spacer(Modifier.weight(1f))

            var showLogoutDialog by remember { mutableStateOf(false) }

            SmartERPGlobalDialog(
                showDialog = showLogoutDialog,
                type = com.kutub.smarterp.ui.components.DialogType.WARNING,
                title = "Logout",
                message = "Are you sure you want to log out of Smart Erp?",
                confirmText = "Logout",
                dismissText = "Cancel",
                onConfirm = {
                    showLogoutDialog = false
                    viewModel.logout()
                    onLogout()
                },
                onDismiss = { showLogoutDialog = false }
            )

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ThemeColorPickerItem(onColorSelect: (String) -> Unit) {
    val dimens = com.kutub.smarterp.ui.theme.LocalDimens.current
    val colors = listOf(
        "#6366F1", // Indigo
        "#10B981", // Emerald
        "#F59E0B", // Amber
        "#EF4444", // Red
        "#3B82F6", // Blue
        "#8B5CF6"  // Violet
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(dimens.paddingLarge)) {
            Text(
                "App Theme Color",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(dimens.paddingMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colors.forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(hex)))
                            .clickable { onColorSelect(hex) }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val dimens = com.kutub.smarterp.ui.theme.LocalDimens.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.paddingLarge)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(com.kutub.smarterp.ui.theme.PrimaryIndigo.copy(alpha = 0.12f), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = com.kutub.smarterp.ui.theme.PrimaryIndigo, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(dimens.paddingLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = com.kutub.smarterp.ui.theme.PrimaryIndigo)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCurrencyItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    currentCurrency: String,
    onCurrencyChange: (String) -> Unit
) {
    val dimens = com.kutub.smarterp.ui.theme.LocalDimens.current
    var showDialog by remember { mutableStateOf(false) }
    val currencies = CurrencyUtils.currencies
    val currentLabel = currencies.find { it.first == currentCurrency }?.second ?: currentCurrency

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.paddingLarge)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(com.kutub.smarterp.ui.theme.PrimaryIndigo.copy(alpha = 0.12f), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = com.kutub.smarterp.ui.theme.PrimaryIndigo, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(dimens.paddingLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Text(
                text = currentLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = dimens.paddingMedium)
            )
        }
    }

    if (showDialog) {
        var searchQuery by remember { mutableStateOf("") }
        val filteredCurrencies = remember(searchQuery) {
            if (searchQuery.isEmpty()) currencies
            else currencies.filter {
                it.second.contains(searchQuery, ignoreCase = true) ||
                        it.first.contains(searchQuery, ignoreCase = true)
            }
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Currency") },
            text = {
                Column(modifier = Modifier.heightIn(max = 450.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search currency...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredCurrencies) { (symbol, label) ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCurrencyChange(symbol)
                                        showDialog = false
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (symbol == currentCurrency)
                                    MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (symbol == currentCurrency)
                                            MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (symbol == currentCurrency) {
                                        Icon(
                                            Icons.Default.Done,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(28.dp)
        )
    }
}




