package com.kutub.smarterp.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.kutub.smarterp.ui.components.DialogType
import com.kutub.smarterp.ui.components.SmartERPGlobalDialog
import com.kutub.smarterp.ui.theme.HeroGradient
import com.kutub.smarterp.ui.theme.PrimaryIndigo
import com.kutub.smarterp.utils.BiometricHelper
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.compose.material.icons.filled.Fingerprint
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showLoginSuccess by remember { mutableStateOf(false) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val fragmentActivity = context as? FragmentActivity
    val isBiometricAvailable = remember { BiometricHelper.isBiometricAvailable(context) }

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

    SmartERPGlobalDialog(
        showDialog = showLoginSuccess,
        type = DialogType.SUCCESS,
        title = "Login Successful",
        message = "Welcome back to Smart Erp!",
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
        // Top Hero Gradient Background Header (Inspired by Daily Finance)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(HeroGradient)
                .padding(24.dp)
        ) {
            // Decorative background translucent circles
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-40).dp, y = 30.dp)
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = com.kutub.smarterp.R.drawable.splash_logo),
                    contentDescription = "Smart ERP Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .border(3.dp, Color.White, CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "SMART ERP",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Business, the Right Way...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // Floating Card Form Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 270.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Enter credentials to access your account",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(28.dp))

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
                                    tint = PrimaryIndigo
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryIndigo,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
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
                                    tint = PrimaryIndigo
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
                                focusedBorderColor = PrimaryIndigo,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { 
                            if (email.isNotBlank() && password.isNotBlank()) {
                                showLoginSuccess = true
                            } else {
                                // Optional: You could set an error message here
                                // loginErrorMessage = "Please enter email and password"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .background(HeroGradient, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Sign In", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    if (isBiometricAvailable && fragmentActivity != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedButton(
                            onClick = { 
                                BiometricHelper.showBiometricPrompt(
                                    activity = fragmentActivity,
                                    onSuccess = { showLoginSuccess = true },
                                    onError = { loginErrorMessage = it }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryIndigo
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo)
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = "Biometric Login")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Login with Biometrics", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Don't have an account? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "Register",
                            color = PrimaryIndigo,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }
                }
            }
        }
    }
}




