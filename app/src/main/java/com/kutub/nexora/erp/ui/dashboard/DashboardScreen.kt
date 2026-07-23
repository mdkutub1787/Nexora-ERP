package com.kutub.nexora.erp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kutub.nexora.erp.R
import com.kutub.nexora.erp.ui.components.DialogType
import com.kutub.nexora.erp.ui.components.NexoraGlobalDialog
import com.kutub.nexora.erp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToSuppliers: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSales: () -> Unit,
    onNavigateToSalesHistory: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val totalProducts by viewModel.totalProducts.collectAsState()
    val lowStockCount by viewModel.lowStockCount.collectAsState()
    val userName by viewModel.userName.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    androidx.activity.compose.BackHandler {
        showExitDialog = true
    }

    NexoraGlobalDialog(
        showDialog = showExitDialog,
        type = DialogType.WARNING,
        title = stringResource(R.string.logout), // Using logout as exit for now or add exit_app
        message = "Are you sure you want to exit Nexora ERP?",
        confirmText = "Exit",
        dismissText = stringResource(R.string.cancel),
        onConfirm = { (context as? android.app.Activity)?.finish() },
        onDismiss = { showExitDialog = false }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.welcome_back),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp, start = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.takeIf { it.isNotBlank() }?.substring(0, 1)?.uppercase() ?: "U",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.dimens.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingLarge)
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingTiny))

            // Quick Stats Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingMedium)
            ) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.in_stock),
                    value = totalProducts.toString(),
                    icon = Icons.Default.Inventory2,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.low_stock),
                    value = lowStockCount.toString(),
                    icon = Icons.Default.ReportProblem,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Text(
                stringResource(R.string.quick_actions),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Actions Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = MaterialTheme.dimens.gridCellMinSize),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingMedium),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ActionItem(
                        title = stringResource(R.string.products),
                        icon = Icons.Default.Inventory,
                        onClick = onNavigateToProducts
                    )
                }
                item {
                    ActionItem(
                        title = stringResource(R.string.categories),
                        icon = Icons.Default.Category,
                        onClick = onNavigateToCategories
                    )
                }
                item {
                    ActionItem(
                        title = stringResource(R.string.suppliers),
                        icon = Icons.Default.LocalShipping,
                        onClick = onNavigateToSuppliers
                    )
                }
                item {
                    ActionItem(
                        title = stringResource(R.string.sales),
                        icon = Icons.Default.PointOfSale,
                        onClick = onNavigateToSales
                    )
                }
                item {
                    ActionItem(
                        title = stringResource(R.string.sales_history),
                        icon = Icons.Default.History,
                        onClick = onNavigateToSalesHistory
                    )
                }
                item {
                    ActionItem(
                        title = stringResource(R.string.reports),
                        icon = Icons.Default.BarChart,
                        onClick = onNavigateToReports
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    val dimens = MaterialTheme.dimens

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
    ) {
        Column(
            modifier = Modifier
                .padding(dimens.paddingMedium)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.paddingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.iconSizeExtraLarge)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(dimens.iconSizeMedium)
                )
            }
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ActionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val dimens = MaterialTheme.dimens

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.paddingMedium),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.iconSizeExtraLarge)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        RoundedCornerShape(dimens.cornerRadiusMedium)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimens.iconSizeMedium)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
