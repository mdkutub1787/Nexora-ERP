package com.kutub.smarterp.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kutub.smarterp.ui.components.DialogType
import com.kutub.smarterp.ui.components.SmartERPGlobalDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userAddress by viewModel.userAddress.collectAsState()
    val userDesignation by viewModel.userDesignation.collectAsState()
    val userImage by viewModel.userImage.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember(userName) { mutableStateOf(userName) }
    var editedEmail by remember(userEmail) { mutableStateOf(userEmail) }
    var editedPhone by remember(userPhone) { mutableStateOf(userPhone) }
    var editedAddress by remember(userAddress) { mutableStateOf(userAddress) }
    var editedDesignation by remember(userDesignation) { mutableStateOf(userDesignation) }
    var editedImageUri by remember(userImage) { mutableStateOf<Uri?>(userImage?.let { Uri.parse(it) }) }

    var showSaveDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val file = java.io.File(context.filesDir, "profile_pic_${System.currentTimeMillis()}.jpg")
                val outputStream = java.io.FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                editedImageUri = android.net.Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
                editedImageUri = it
            }
        }
    }

    SmartERPGlobalDialog(
        showDialog = showSaveDialog,
        type = DialogType.SUCCESS,
        title = "Profile Saved!",
        message = "Your profile information has been successfully updated.",
        confirmText = "OK",
        onConfirm = { showSaveDialog = false },
        onDismiss = { showSaveDialog = false }
    )

    SmartERPGlobalDialog(
        showDialog = showLogoutDialog,
        type = DialogType.WARNING,
        title = "Logout",
        message = "Are you sure you want to log out of SmartERP ERP?",
        confirmText = "Logout",
        dismissText = "Cancel",
        onConfirm = {
            showLogoutDialog = false
            viewModel.logout()
            onLogout()
        },
        onDismiss = { showLogoutDialog = false }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        TextButton(onClick = { 
                            isEditing = false 
                            // Reset edited values if needed, or keep them
                        }) {
                            Text("Cancel", color = MaterialTheme.colorScheme.error)
                        }
                    } else {
                        TextButton(onClick = { isEditing = true }) {
                            Text("Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(com.kutub.smarterp.ui.theme.HeroGradient)
                    .clickable(enabled = isEditing) {
                        photoPickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (editedImageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(editedImageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = userName.takeIf { it.isNotBlank() }?.substring(0, 1)?.uppercase() ?: "U",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
            }

            Text(
                text = if (isEditing) "Tap to change photo" else userDesignation,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isEditing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            ProfileInputField(
                value = if (isEditing) editedName else userName,
                onValueChange = { editedName = it },
                label = "Full Name",
                icon = Icons.Default.Person,
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                value = if (isEditing) editedDesignation else userDesignation,
                onValueChange = { editedDesignation = it },
                label = "Designation",
                icon = Icons.Default.Work,
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                value = if (isEditing) editedEmail else userEmail,
                onValueChange = { editedEmail = it },
                label = "Email Address",
                icon = Icons.Default.Email,
                enabled = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                value = if (isEditing) editedPhone else userPhone,
                onValueChange = { editedPhone = it },
                label = "Phone Number",
                icon = Icons.Default.Phone,
                enabled = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                value = if (isEditing) editedAddress else userAddress,
                onValueChange = { editedAddress = it },
                label = "Address",
                icon = Icons.Default.LocationOn,
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isEditing) {
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            editedName,
                            editedEmail,
                            editedPhone,
                            editedAddress,
                            editedDesignation,
                            editedImageUri?.toString()
                        )
                        isEditing = false
                        showSaveDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Logout from SmartERP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    )
}


