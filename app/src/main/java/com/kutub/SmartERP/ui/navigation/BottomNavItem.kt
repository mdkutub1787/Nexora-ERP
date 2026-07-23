package com.kutub.smarterp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem("dashboard_route", "Dashboard", Icons.Default.Dashboard)
    object Inventory : BottomNavItem("product_list_route", "Inventory", Icons.Default.Inventory)
    object POS : BottomNavItem("pos_route", "POS", Icons.Default.PointOfSale)
    object History : BottomNavItem("sales_history_route", "History", Icons.Default.History)
    object Settings : BottomNavItem("settings_route", "Settings", Icons.Default.Settings)
}


