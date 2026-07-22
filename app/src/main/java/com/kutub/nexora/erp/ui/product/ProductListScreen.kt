package com.kutub.nexora.erp.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.nexora.erp.data.model.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateToAddProduct: (Long?) -> Unit, viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currency by viewModel.currency.collectAsState()

    var selectedProduct by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf<ProductEntity?>(
            null
        )
    }
    var showDeleteConfirm by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(
            false
        )
    }

    if (selectedProduct != null && !showDeleteConfirm) {
        AlertDialog(onDismissRequest = { selectedProduct = null }, title = {
            Text(
                selectedProduct?.name ?: "Product Details", fontWeight = FontWeight.Bold
            )
        }, text = {
            selectedProduct?.let {
                Column {
                    Text("Price: $currency${it.price}")
                    Text("Cost Price: $currency${it.costPrice}")
                    Text("Stock: ${it.stockQuantity}")
                    Text("Barcode: ${it.barcode}")
                    Text("SKU: ${it.sku}")
                }
            }
        }, confirmButton = {
            TextButton(onClick = { selectedProduct = null }) {
                Text("Close")
            }
        }, dismissButton = {
            Row {
                TextButton(onClick = { 
                    selectedProduct?.let { onNavigateToAddProduct(it.id) }
                    selectedProduct = null
                }) {
                    Text("Edit", color = MaterialTheme.colorScheme.primary)
                }
                TextButton(onClick = { showDeleteConfirm = true }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        })
    }

    com.kutub.nexora.erp.ui.components.NexoraGlobalDialog(
        showDialog = showDeleteConfirm,
        type = com.kutub.nexora.erp.ui.components.DialogType.WARNING,
        title = "Delete Product",
        message = "Are you sure you want to delete ${selectedProduct?.name}?",
        confirmText = "Delete",
        dismissText = "Cancel",
        onConfirm = {
            selectedProduct?.let { viewModel.deleteProduct(it) }
            showDeleteConfirm = false
            selectedProduct = null
        },
        onDismiss = { showDeleteConfirm = false })

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { onNavigateToAddProduct(null) }, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Product")
        }
    }, topBar = {
        TopAppBar(
            title = { Text("Products", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            currency = currency,
                            onClick = { selectedProduct = product }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    currency: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Stock: ${product.stockQuantity} | Price: $currency${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
