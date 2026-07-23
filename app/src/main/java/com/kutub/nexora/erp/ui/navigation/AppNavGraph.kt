package com.kutub.nexora.erp.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kutub.nexora.erp.ui.auth.LoginScreen
import com.kutub.nexora.erp.ui.auth.RegisterScreen
import com.kutub.nexora.erp.ui.auth.SplashScreen
import com.kutub.nexora.erp.ui.dashboard.DashboardScreen
import com.kutub.nexora.erp.ui.product.AddEditProductScreen
import com.kutub.nexora.erp.ui.product.ProductListScreen
import com.kutub.nexora.erp.ui.profile.ProfileScreen
import com.kutub.nexora.erp.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController, startDestination: String = "splash_route"
) {
    val bottomNavItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Inventory,
        BottomNavItem.POS,
        BottomNavItem.History,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val shouldShowBottomBar = bottomNavItems.any { it.route == currentRoute }

            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = {
                                Text(
                                    screen.title, style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            })
                    }
                }
            }
        }, contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()) // Only pad the bottom
        ) {
            composable("splash_route") {
                SplashScreen(onNavigateToLogin = {
                    navController.navigate("auth_route") {
                        popUpTo("splash_route") { inclusive = true }
                    }
                }, onNavigateToDashboard = {
                    navController.navigate("dashboard_route") {
                        popUpTo("splash_route") { inclusive = true }
                    }
                })
            }
            composable("auth_route") {
                LoginScreen(onNavigateToDashboard = {
                    navController.navigate("dashboard_route") {
                        popUpTo("auth_route") { inclusive = true }
                    }
                }, onNavigateToRegister = {
                    navController.navigate("register_route")
                })
            }
            composable("register_route") {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    })
            }
            composable("dashboard_route") {
                DashboardScreen(
                    onNavigateToProducts = { navController.navigate("product_list_route") },
                    onNavigateToSuppliers = { navController.navigate("supplier_list_route") },
                    onNavigateToCategories = { navController.navigate("category_list_route") },
                    onNavigateToSales = { navController.navigate("pos_route") },
                    onNavigateToSalesHistory = { navController.navigate("sales_history_route") },
                    onNavigateToReports = { navController.navigate("reports_route") },
                    onNavigateToSettings = { navController.navigate("settings_route") },
                    onNavigateToProfile = { navController.navigate("profile_route") })
            }

            composable("pos_route") {
                com.kutub.nexora.erp.ui.pos.PosScreen(
                    onNavigateBack = { navController.popBackStack() })
            }
            composable("profile_route") {
                ProfileScreen(onNavigateBack = { navController.popBackStack() }, onLogout = {
                    navController.navigate("auth_route") {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                    }
                })
            }
            composable("settings_route") {
                SettingsScreen(onNavigateBack = {
                    navController.popBackStack()
                }, onLogout = {
                    navController.navigate("auth_route") {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
            composable("product_list_route") {
                ProductListScreen(
                    onNavigateToAddProduct = { productId ->
                        if (productId != null) {
                            navController.navigate("add_edit_product_route?productId=$productId")
                        } else {
                            navController.navigate("add_edit_product_route")
                        }
                    })
            }
            composable(
                route = "add_edit_product_route?productId={productId}", arguments = listOf(
                androidx.navigation.navArgument("productId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                })) { backStackEntry ->
                val productIdStr = backStackEntry.arguments?.getString("productId")
                val productId = productIdStr?.toLongOrNull()

                AddEditProductScreen(
                    productId = productId, onNavigateBack = {
                        navController.popBackStack()
                    })
            }

            composable("category_list_route") {
                com.kutub.nexora.erp.ui.category.CategoryListScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddEditCategory = { categoryId ->
                        if (categoryId != null) {
                            navController.navigate("add_edit_category_route?categoryId=$categoryId")
                        } else {
                            navController.navigate("add_edit_category_route")
                        }
                    })
            }
            composable(
                route = "add_edit_category_route?categoryId={categoryId}",
                arguments = listOf(
                    androidx.navigation.navArgument("categoryId") {
                        type = androidx.navigation.NavType.StringType
                        nullable = true
                    })) { backStackEntry ->
                val categoryIdStr = backStackEntry.arguments?.getString("categoryId")
                val categoryId = categoryIdStr?.toLongOrNull()

                com.kutub.nexora.erp.ui.category.AddEditCategoryScreen(
                    categoryId = categoryId, onNavigateBack = {
                        navController.popBackStack()
                    })
            }

            composable("supplier_list_route") {
                com.kutub.nexora.erp.ui.supplier.SupplierListScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddEditSupplier = { supplierId ->
                        if (supplierId != null) {
                            navController.navigate("add_edit_supplier_route?supplierId=$supplierId")
                        } else {
                            navController.navigate("add_edit_supplier_route")
                        }
                    })
            }
            composable(
                route = "add_edit_supplier_route?supplierId={supplierId}",
                arguments = listOf(
                    androidx.navigation.navArgument("supplierId") {
                        type = androidx.navigation.NavType.StringType
                        nullable = true
                    })) { backStackEntry ->
                val supplierIdStr = backStackEntry.arguments?.getString("supplierId")
                val supplierId = supplierIdStr?.toLongOrNull()

                com.kutub.nexora.erp.ui.supplier.AddEditSupplierScreen(
                    supplierId = supplierId, onNavigateBack = {
                        navController.popBackStack()
                    })
            }

            composable("sales_history_route") {
                com.kutub.nexora.erp.ui.sales.SalesHistoryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSaleDetail = { saleId ->
                        navController.navigate("sale_detail_route/$saleId")
                    })
            }

            composable(
                route = "sale_detail_route/{saleId}", arguments = listOf(
                androidx.navigation.navArgument("saleId") {
                    type = androidx.navigation.NavType.LongType
                })) { backStackEntry ->
                val saleId = backStackEntry.arguments?.getLong("saleId") ?: return@composable
                com.kutub.nexora.erp.ui.sales.SaleDetailScreen(
                    saleId = saleId, onNavigateBack = { navController.popBackStack() })
            }

            composable("reports_route") {
                com.kutub.nexora.erp.ui.reports.ReportsScreen(
                    onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
