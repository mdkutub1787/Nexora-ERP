package com.kutub.nexora.erp.ui.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import com.kutub.nexora.erp.data.model.ProductEntity
import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog
import com.kutub.nexora.erp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    onNavigateBack: () -> Unit,
    viewModel: PosViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val currency by viewModel.currency.collectAsState(initial = "$")

    var showCartSheet by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var completedSaleReceipt by remember { mutableStateOf<com.kutub.nexora.erp.data.model.SaleEntity?>(null) }
    var completedSaleItems by remember { mutableStateOf<List<com.kutub.nexora.erp.data.model.SaleItemEntity>>(emptyList()) }

    if (completedSaleReceipt != null) {
        ReceiptDialog(
            sale = completedSaleReceipt!!,
            saleItems = completedSaleItems,
            currency = currency,
            onDismiss = {
                completedSaleReceipt = null
                completedSaleItems = emptyList()
            }
        )
    }

    if (showCheckoutDialog) {
        CheckoutDialog(
            totalAmount = totalAmount,
            currency = currency,
            onDismiss = { showCheckoutDialog = false },
            onConfirm = { customerName, discount ->
                viewModel.checkout(customerName, discount) { sale, items ->
                    showCheckoutDialog = false
                    showCartSheet = false
                    completedSaleReceipt = sale
                    completedSaleItems = items
                }
            }
        )
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Barcode Launcher
    val barcodeLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        com.journeyapps.barcodescanner.ScanContract()
    ) { result ->
        if (result.contents != null) {
            val scannedSku = result.contents
            val matchedProduct = products.find { it.sku == scannedSku }
            if (matchedProduct != null) {
                viewModel.addToCart(matchedProduct)
                android.widget.Toast.makeText(context, "Added ${matchedProduct.name} to cart", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.widget.Toast.makeText(context, "Product not found for SKU: $scannedSku", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Point of Sale (POS)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val options = com.journeyapps.barcodescanner.ScanOptions()
                        options.setDesiredBarcodeFormats(com.journeyapps.barcodescanner.ScanOptions.ALL_CODE_TYPES)
                        options.setPrompt("Scan a barcode")
                        options.setCameraId(0)
                        options.setBeepEnabled(true)
                        options.setBarcodeImageEnabled(false)
                        barcodeLauncher.launch(options)
                    }) {
                        Icon(Icons.Default.QrCode, contentDescription = "Scan Barcode")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCartSheet = true },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                text = { Text("Cart (${cartItems.sumOf { it.quantity }}) - ${currency}${totalAmount}") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val dimens = MaterialTheme.dimens
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = dimens.gridCellMinSize),
                contentPadding = PaddingValues(dimens.paddingMedium),
                horizontalArrangement = Arrangement.spacedBy(dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(dimens.paddingMedium)
            ) {
                items(products) { product ->
                    PosProductCard(
                        product = product,
                        currency = currency,
                        onClick = { viewModel.addToCart(product) }
                    )
                }
            }
        }
    }

    if (showCartSheet) {
        val dimens = MaterialTheme.dimens
        ModalBottomSheet(
            onDismissRequest = { showCartSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.paddingMedium)
                    .padding(bottom = dimens.paddingExtraLarge)
            ) {
                Text(
                    text = "Your Cart",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = dimens.paddingMedium)
                )

                if (cartItems.isEmpty()) {
                    Text(
                        text = "Cart is empty",
                        modifier = Modifier.padding(vertical = dimens.paddingExtraLarge),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(dimens.paddingSmall)
                    ) {
                        items(cartItems) { item ->
                            CartItemRow(
                                item = item,
                                currency = currency,
                                onAdd = { viewModel.addToCart(item.product) },
                                onRemove = { viewModel.removeFromCart(item.product) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimens.paddingMedium))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(dimens.paddingMedium))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currency}${totalAmount}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.paddingLarge))

                    Button(
                        onClick = { showCheckoutDialog = true },
                        modifier = Modifier.fillMaxWidth().height(dimens.buttonHeight),
                        shape = RoundedCornerShape(dimens.cornerRadiusSmall)
                    ) {
                        Text("Proceed to Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PosProductCard(
    product: ProductEntity,
    currency: String,
    onClick: () -> Unit
) {
    val dimens = MaterialTheme.dimens
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = product.stockQuantity > 0) { onClick() },
        shape = RoundedCornerShape(dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = if (product.stockQuantity > 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
    ) {
        Column(
            modifier = Modifier.padding(dimens.paddingMedium)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (product.stockQuantity > 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = product.name.takeIf { it.isNotBlank() }?.substring(0, 1)?.uppercase() ?: "P",
                        fontWeight = FontWeight.Bold,
                        color = if (product.stockQuantity > 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "SKU: ${product.sku ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.paddingMedium))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(dimens.paddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currency}${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (product.stockQuantity > 0) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = "Stock: ${product.stockQuantity}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (product.stockQuantity > 0) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    currency: String,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${currency}${item.product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Remove", modifier = Modifier.size(20.dp))
            }
            
            Text(
                text = "${item.quantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun CheckoutDialog(
    totalAmount: Double,
    currency: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var discountStr by remember { mutableStateOf("0.0") }
    
    val discount = discountStr.toDoubleOrNull() ?: 0.0
    val finalAmount = (totalAmount - discount).coerceAtLeast(0.0)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                OutlinedTextField(
                    value = discountStr,
                    onValueChange = { discountStr = it },
                    label = { Text("Discount Amount ($currency)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                HorizontalDivider()
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${currency}${totalAmount}", fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("-${currency}${discount}", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Final Amount:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("${currency}${finalAmount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    
                    Button(
                        onClick = { onConfirm(customerName, discount) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Confirm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ReceiptDialog(
    sale: com.kutub.nexora.erp.data.model.SaleEntity,
    saleItems: List<com.kutub.nexora.erp.data.model.SaleItemEntity>,
    currency: String,
    onDismiss: () -> Unit
) {
    val dimens = com.kutub.nexora.erp.ui.theme.LocalDimens.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(dimens.paddingMedium),
            shape = RoundedCornerShape(dimens.cornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Receipt",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimens.iconSizeExtraLarge)
                )
                Spacer(Modifier.height(dimens.paddingSmall))
                Text(androidx.compose.ui.res.stringResource(com.kutub.nexora.erp.R.string.app_name), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Digital Receipt", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(Modifier.height(dimens.paddingMedium))
                HorizontalDivider()
                Spacer(Modifier.height(dimens.paddingMedium))

                // Items
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(saleItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.productName} x${item.quantity}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text("${currency}${item.totalPrice}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(dimens.paddingMedium))
                HorizontalDivider()
                Spacer(Modifier.height(dimens.paddingMedium))

                // Totals
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal:", style = MaterialTheme.typography.bodyLarge)
                    Text("${currency}${sale.totalAmount}", style = MaterialTheme.typography.bodyLarge)
                }
                if (sale.discount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Discount:", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        Text("-${currency}${sale.discount}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                    }
                }
                Spacer(Modifier.height(dimens.paddingSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("${currency}${sale.finalAmount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(Modifier.height(dimens.paddingLarge))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimens.cornerRadiusMedium)
                ) {
                    Text("Done", modifier = Modifier.padding(vertical = 8.dp), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
