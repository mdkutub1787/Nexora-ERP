package com.kutub.smarterp.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kutub.smarterp.data.model.ProductEntity
import com.kutub.smarterp.ui.components.DialogType
import com.kutub.smarterp.ui.components.SmartERPGlobalDialog
import com.kutub.smarterp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateToAddProduct: (Long?) -> Unit, viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currency by viewModel.currency.collectAsState()

    var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDeleteSuccess by remember { mutableStateOf(false) }

    var showCameraScanner by remember { mutableStateOf(false) }

    if (showCameraScanner) {
        com.kutub.smarterp.ui.components.CameraScannerDialog(onDismiss = {
            showCameraScanner = false
        }, onBarcodeScanned = { scannedBarcode ->
            viewModel.onSearchQueryChange(scannedBarcode)
        })
    }

    SmartERPGlobalDialog(
        showDialog = showDeleteConfirm,
        type = DialogType.WARNING,
        title = "Delete Product",
        message = "Are you sure you want to delete ${selectedProduct?.name}?",
        confirmText = "Delete",
        dismissText = "Cancel",
        onConfirm = {
            selectedProduct?.let { viewModel.deleteProduct(it) }
            showDeleteConfirm = false
            selectedProduct = null
            showDeleteSuccess = true
        },
        onDismiss = { showDeleteConfirm = false })

    SmartERPGlobalDialog(
        showDialog = showDeleteSuccess,
        type = DialogType.SUCCESS,
        title = "Deleted",
        message = "Product deleted successfully.",
        confirmText = "OK",
        onConfirm = { showDeleteSuccess = false },
        onDismiss = { showDeleteSuccess = false })

    if (selectedProduct != null && !showDeleteConfirm) {
        ModalBottomSheet(
            onDismissRequest = { selectedProduct = null },
            sheetState = rememberModalBottomSheetState()
        ) {
            ProductDetailsContent(product = selectedProduct!!, currency = currency, onEdit = {
                onNavigateToAddProduct(selectedProduct!!.id)
                selectedProduct = null
            }, onDelete = { showDeleteConfirm = true })
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Inventory", fontWeight = FontWeight.Bold) }, actions = {
                IconButton(onClick = { showCameraScanner = true }) {
                    Icon(Icons.Default.QrCode, contentDescription = "Scan Barcode")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { onNavigateToAddProduct(null) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Product")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            val dimens = MaterialTheme.dimens
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier.padding(dimens.paddingMedium)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.products.isEmpty()) {
                EmptyState(message = if (searchQuery.isBlank()) "Your inventory is empty" else "No matching products found")
            } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            currency = currency,
                            onClick = { selectedProduct = product })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search by name, SKU...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
fun ProductCard(
    product: ProductEntity, currency: String, onClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (product.stockQuantity > 0) primaryColor.copy(alpha = 0.12f)
                                else com.kutub.smarterp.ui.theme.ExpenseRed.copy(alpha = 0.12f)
                            ), contentAlignment = Alignment.Center
                    ) {
                        if (!product.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                error = androidx.compose.ui.res.painterResource(com.kutub.smarterp.R.drawable.splash_logo),
                                placeholder = androidx.compose.ui.res.painterResource(com.kutub.smarterp.R.drawable.splash_logo)
                            )
                        } else {
                            Text(
                                text = product.name.takeIf { it.isNotBlank() }?.substring(0, 1)
                                    ?.uppercase() ?: "P",
                                fontWeight = FontWeight.Bold,
                                color = if (product.stockQuantity > 0) primaryColor else com.kutub.smarterp.ui.theme.ExpenseRed,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "SKU: ${product.sku ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (product.stockQuantity > 5) com.kutub.smarterp.ui.theme.IncomeGreen.copy(alpha = 0.15f)
                    else if (product.stockQuantity in 1..5) com.kutub.smarterp.ui.theme.WarningAmber.copy(alpha = 0.15f)
                    else com.kutub.smarterp.ui.theme.ExpenseRed.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (product.stockQuantity > 0) "Stock: ${product.stockQuantity}" else "Out of Stock",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (product.stockQuantity > 5) com.kutub.smarterp.ui.theme.IncomeGreen
                        else if (product.stockQuantity in 1..5) com.kutub.smarterp.ui.theme.WarningAmber
                        else com.kutub.smarterp.ui.theme.ExpenseRed,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Purchase Cost",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$currency${product.costPrice}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Selling Price",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$currency${product.price}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailsContent(
    product: ProductEntity, currency: String, onEdit: () -> Unit, onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Product Details",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        DetailItem(
            label = "Price", value = "$currency${product.price}", icon = Icons.Default.Payments
        )
        DetailItem(
            label = "Cost Price",
            value = "$currency${product.costPrice}",
            icon = Icons.Default.AccountBalanceWallet
        )
        DetailItem(
            label = "Current Stock",
            value = "${product.stockQuantity} units",
            icon = Icons.Default.Inventory2
        )
        DetailItem(label = "Barcode", value = product.barcode ?: "N/A", icon = Icons.Default.QrCode)
        DetailItem(label = "SKU", value = product.sku ?: "N/A", icon = Icons.Default.Tag)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Delete")
            }
            Button(
                onClick = onEdit, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Edit Product")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Inventory2,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


