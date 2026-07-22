package com.kutub.nexora.erp.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: Long? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val currency by viewModel.currency.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    NexoraGlobalDialog(
        showDialog = showSuccessDialog,
        type = DialogType.SUCCESS,
        title = "Success",
        message = if (productId != null) "Product updated successfully." else "Product saved successfully.",
        confirmText = "OK",
        onConfirm = {
            showSuccessDialog = false
            onNavigateBack()
        },
        onDismiss = { showSuccessDialog = false }
    )

    NexoraGlobalDialog(
        showDialog = showErrorDialog,
        type = DialogType.ERROR,
        title = "Error",
        message = "Please fill in all fields before saving.",
        confirmText = "Got it",
        onConfirm = { showErrorDialog = false },
        onDismiss = { showErrorDialog = false }
    )

    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProduct(productId)
            if (product != null) {
                name = product.name
                price = product.price.toString()
                stock = product.stockQuantity.toString()
                imageUrl = product.imageUrl ?: ""
            }
        }
    }

    Scaffold(modifier = Modifier.imePadding(), topBar = {
        TopAppBar(
            title = { Text(if (productId != null) "Edit Product" else "Add Product", fontWeight = FontWeight.Bold) }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }, bottomBar = {
        Box(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    if (name.isNotBlank() && price.isNotBlank() && stock.isNotBlank()) {
                        viewModel.saveProduct(productId, name, price, stock, imageUrl.ifBlank { null })
                        showSuccessDialog = true
                    } else {
                        showErrorDialog = true
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (productId != null) "Update Product" else "Save Product", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
        }
    }) { paddingValues ->
        val scrollState = androidx.compose.foundation.rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Product Image URL") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("https://example.com/image.jpg") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Initial Stock") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
