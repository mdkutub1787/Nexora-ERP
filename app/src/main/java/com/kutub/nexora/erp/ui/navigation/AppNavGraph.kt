package com.kutub.nexora.erp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kutub.nexora.erp.ui.auth.LoginScreen
import com.kutub.nexora.erp.ui.auth.RegisterScreen
import com.kutub.nexora.erp.ui.auth.SplashScreen
import com.kutub.nexora.erp.ui.dashboard.DashboardScreen
import com.kutub.nexora.erp.ui.product.AddEditProductScreen
import com.kutub.nexora.erp.ui.product.ProductListScreen
import com.kutub.nexora.erp.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = "splash_route"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash_route") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("auth_route") {
                        popUpTo("splash_route") { inclusive = true }
                    }
                }
            )
        }
        composable("auth_route") {
            LoginScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard_route") {
                        popUpTo("auth_route") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register_route")
                }
            )
        }
        composable("register_route") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("dashboard_route") {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate("product_list_route")
                },
                onNavigateToSettings = {
                    navController.navigate("settings_route")
                }
            )
        }
        composable("settings_route") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("product_list_route") {
            ProductListScreen(
                onNavigateToAddProduct = {
                    navController.navigate("add_product_route")
                }
            )
        }
        composable("add_product_route") {
            AddEditProductScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
