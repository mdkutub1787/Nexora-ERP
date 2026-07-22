package com.kutub.nexora.erp.ui.supplier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSupplierScreen(
    supplierId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: SupplierViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(true) }

    var showSaveSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(supplierId) {
        if (supplierId != null) {
            viewModel.getSupplierById(supplierId)?.let { supplier ->
                name = supplier.name
                contactName = supplier.contactName ?: ""
                phone = supplier.phone
                email = supplier.email ?: ""
                address = supplier.address ?: ""
                status = supplier.status
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    NexoraGlobalDialog(
        showDialog = showSaveSuccess,
        type = DialogType.SUCCESS,
        title = "Success",
        message = if (supplierId != null) "Supplier updated successfully." else "Supplier added successfully.",
        confirmText = "OK",
        onConfirm = {
            showSaveSuccess = false
            onNavigateBack()
        },
        onDismiss = {
            showSaveSuccess = false
            onNavigateBack()
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (supplierId != null) "Edit Supplier" else "Add Supplier", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Company / Supplier Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = contactName,
                onValueChange = { contactName = it },
                label = { Text("Contact Person Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = status, onCheckedChange = { status = it })
                Text("Active Supplier")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        coroutineScope.launch {
                            if (supplierId != null) {
                                viewModel.getSupplierById(supplierId)?.let {
                                    viewModel.updateSupplier(it.copy(
                                        name = name,
                                        contactName = contactName.takeIf { c -> c.isNotBlank() },
                                        phone = phone,
                                        email = email.takeIf { e -> e.isNotBlank() },
                                        address = address.takeIf { a -> a.isNotBlank() },
                                        status = status,
                                        updatedAt = System.currentTimeMillis()
                                    ))
                                    showSaveSuccess = true
                                }
                            } else {
                                viewModel.insertSupplier(
                                    name = name,
                                    contactName = contactName.takeIf { c -> c.isNotBlank() },
                                    phone = phone,
                                    email = email.takeIf { e -> e.isNotBlank() },
                                    address = address.takeIf { a -> a.isNotBlank() },
                                    status = status
                                )
                                showSaveSuccess = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Text(if (supplierId != null) "Update Supplier" else "Save Supplier", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
