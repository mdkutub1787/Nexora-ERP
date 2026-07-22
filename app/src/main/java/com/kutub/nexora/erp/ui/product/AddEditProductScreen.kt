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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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

    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProduct(productId)
            if (product != null) {
                name = product.name
                price = product.price.toString()
                stock = product.stockQuantity.toString()
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
                        viewModel.saveProduct(productId, name, price, stock)
                        onNavigateBack()
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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
