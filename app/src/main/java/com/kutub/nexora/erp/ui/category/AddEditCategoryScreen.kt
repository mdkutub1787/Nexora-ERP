package com.kutub.nexora.erp.ui.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    categoryId: Long?, onNavigateBack: () -> Unit, viewModel: CategoryViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showSaveSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(categoryId) {
        if (categoryId != null) {
            viewModel.getCategoryById(categoryId)?.let { category ->
                name = category.name
                description = category.description ?: ""
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    NexoraGlobalDialog(
        showDialog = showSaveSuccess,
        type = DialogType.SUCCESS,
        title = "Success",
        message = if (categoryId != null) "Category updated successfully." else "Category added successfully.",
        confirmText = "OK",
        onConfirm = {
            showSaveSuccess = false
            onNavigateBack()
        },
        onDismiss = {
            showSaveSuccess = false
            onNavigateBack()
        })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    if (categoryId != null) "Edit Category" else "Add Category",
                    fontWeight = FontWeight.Bold
                )
            }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
            )
        }) { paddingValues ->
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
                label = { Text("Category Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            if (categoryId != null) {
                                viewModel.getCategoryById(categoryId)?.let {
                                    viewModel.updateCategory(
                                        it.copy(
                                            name = name,
                                            description = description.takeIf { d -> d.isNotBlank() },
                                            updatedAt = System.currentTimeMillis()
                                        )
                                    )
                                    showSaveSuccess = true
                                }
                            } else {
                                viewModel.insertCategory(
                                    name = name,
                                    description = description.takeIf { it.isNotBlank() })
                                showSaveSuccess = true
                            }
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), enabled = name.isNotBlank()
            ) {
                Text(
                    if (categoryId != null) "Update Category" else "Save Category",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
