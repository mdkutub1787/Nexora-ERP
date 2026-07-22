package com.kutub.nexora.erp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.repository.ProductRepository
import com.kutub.nexora.erp.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userName: StateFlow<String> = preferencesManager.userNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "User")

    val totalProducts: StateFlow<Int> = productRepository.getProductsCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val lowStockCount: StateFlow<Int> = productRepository.getAllProducts()
        .map { products -> products.count { it.stockQuantity < 10 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
}
