package com.kutub.nexora.erp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.repository.ProductRepository
import com.kutub.nexora.erp.data.repository.SaleRepository
import com.kutub.nexora.erp.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userName: StateFlow<String> = preferencesManager.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "User"
    )

    val currency: StateFlow<String> = preferencesManager.currencyFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "$"
    )

    val totalProducts: StateFlow<Int> = productRepository.getProductsCount().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0
    )

    val totalInventoryValue: StateFlow<Double> = productRepository.getAllProducts()
        .map { products -> products.sumOf { it.price * it.stockQuantity } }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0.0)

    val lowStockCount: StateFlow<Int> = productRepository.getAllProducts()
        .map { products -> products.count { it.stockQuantity < 10 } }.stateIn(
            scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0
        )

    val todayRevenue: StateFlow<Double> = saleRepository.getAllSales()
        .map { sales ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val todayStart = calendar.timeInMillis
            sales.filter { it.saleDate >= todayStart }.sumOf { it.finalAmount }
        }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0.0)

    val recentSales: StateFlow<List<com.kutub.nexora.erp.data.model.SaleEntity>> = saleRepository.getAllSales()
        .map { sales -> sales.sortedByDescending { it.saleDate }.take(5) }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())
}
