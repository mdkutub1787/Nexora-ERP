package com.kutub.nexora.erp.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("mdkutub150@gmail.com") }
    var password by remember { mutableStateOf("000000") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showLoginSuccess by remember { mutableStateOf(false) }

    // Autofill Setup
    val autofill = LocalAutofill.current
    val autofillTree = LocalAutofillTree.current

    val emailAutofillNode = remember {
        AutofillNode(
            onFill = { email = it }, autofillTypes = listOf(AutofillType.EmailAddress)
        )
    }
    val passwordAutofillNode = remember {
        AutofillNode(
            onFill = { password = it }, autofillTypes = listOf(AutofillType.Password)
        )
    }

    DisposableEffect(Unit) {
        autofillTree.children[1] = emailAutofillNode
        autofillTree.children[2] = passwordAutofillNode
        onDispose {
            autofillTree.children.remove(1)
            autofillTree.children.remove(2)
        }
    }

    // Professional Auto-Login Logic
    if (showLoginSuccess) {
        LaunchedEffect(Unit) {
            delay(1200) // Brief success display
            viewModel.login(email = email)
            onNavigateToDashboard()
            showLoginSuccess = false
        }
    }

    NexoraGlobalDialog(
        showDialog = showLoginSuccess,
        type = DialogType.SUCCESS,
        title = "Login Successful",
        message = "Welcome back to Nexora ERP!",
        confirmText = null, // Hide button for auto-login
        onConfirm = null,
        onDismiss = { }
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.kutub.nexora.erp.R.drawable.splash_logo),
                    contentDescription = "Nexora ERP Logo",
                    modifier = Modifier.fillMaxSize(0.7f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Sign in to manage your enterprise",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            emailAutofillNode.boundingBox =
                                it.localToWindow(androidx.compose.ui.geometry.Offset.Zero)
                                    .let { offset ->
                                        Rect(
                                            offset.x,
                                            offset.y,
                                            offset.x + it.size.width,
                                            offset.y + it.size.height
                                        )
                                    }
                        }
                        .onFocusChanged { focusState ->
                            autofill?.run {
                                if (focusState.isFocused) {
                                    requestAutofillForNode(emailAutofillNode)
                                } else {
                                    cancelAutofillForNode(emailAutofillNode)
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            passwordAutofillNode.boundingBox =
                                it.localToWindow(androidx.compose.ui.geometry.Offset.Zero)
                                    .let { offset ->
                                        Rect(
                                            offset.x,
                                            offset.y,
                                            offset.x + it.size.width,
                                            offset.y + it.size.height
                                        )
                                    }
                        }
                        .onFocusChanged { focusState ->
                            autofill?.run {
                                if (focusState.isFocused) {
                                    requestAutofillForNode(passwordAutofillNode)
                                } else {
                                    cancelAutofillForNode(passwordAutofillNode)
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Forgot Password?",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showLoginSuccess = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable { onNavigateToRegister() })
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
