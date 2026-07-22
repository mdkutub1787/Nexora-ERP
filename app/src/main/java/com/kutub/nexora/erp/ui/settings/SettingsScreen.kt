package com.kutub.nexora.erp.ui.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.nexora.erp.utils.BiometricHelper
import com.kutub.nexora.erp.utils.CurrencyUtils
import com.kutub.nexora.erp.utils.LocaleHelper

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SettingsToggleItem(
                title = "Biometric Lock",
                subtitle = "Use fingerprint to unlock the app",
                icon = Icons.Default.Fingerprint,
                checked = isBiometricEnabled,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        if (BiometricHelper.isBiometricAvailable(context)) {
                            viewModel.setBiometricEnabled(true)
                        } else {
                            Toast.makeText(context, "Biometric setup is not available on this device.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        viewModel.setBiometricEnabled(false)
                    }
                }
            )

            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SettingsCurrencyItem(
                title = "Currency",
                subtitle = "Select default currency for products",
                icon = Icons.Default.AttachMoney,
                currentCurrency = currentCurrency,
                onCurrencyChange = { viewModel.setCurrency(it) }
            )

            Spacer(Modifier.height(16.dp))

            SettingsToggleItem(
                title = "Dark Mode",
                subtitle = "Toggle dark/light theme",
                icon = Icons.Default.DarkMode,
                checked = currentTheme == "dark",
                onCheckedChange = { isDark -> viewModel.setThemeMode(if (isDark) "dark" else "light") }
            )
            
            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.weight(1f))

            var showLogoutDialog by remember { mutableStateOf(false) }

            NexoraGlobalDialog(
                showDialog = showLogoutDialog,
                type = com.kutub.nexora.erp.ui.components.DialogType.WARNING,
                title = "Logout",
                message = "Are you sure you want to log out of Nexora ERP?",
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
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
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
    var showDialog by remember { mutableStateOf(false) }
    val currencies = CurrencyUtils.currencies
    val currentLabel = currencies.find { it.first == currentCurrency }?.second ?: currentCurrency

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Text(
                text = currentLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
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
