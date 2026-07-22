package com.kutub.nexora.erp.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.model.ProductEntity
import com.kutub.nexora.erp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.kutub.nexora.erp.data.local.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    
    val currency = preferencesManager.currencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "$"
    )

    private val _products = productRepository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState = combine(_searchQuery, _products) { query, products ->
        val filtered = if (query.isBlank()) {
            products
        } else {
            products.filter { it.name.contains(query, ignoreCase = true) }
        }
        ProductUiState(products = filtered, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductUiState(isLoading = true))

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }
    
    suspend fun getProduct(id: Long): ProductEntity? {
        return productRepository.getProductById(id).firstOrNull()
    }

    fun saveProduct(id: Long?, name: String, price: String, stock: String) {
        if (name.isBlank() || price.isBlank() || stock.isBlank()) return
        
        viewModelScope.launch {
            val entity = ProductEntity(
                id = id ?: 0,
                name = name,
                barcode = "BAR" + (100000..999999).random(), // Generate random barcode for now
                sku = "SKU" + (100..999).random(),
                price = price.toDoubleOrNull() ?: 0.0,
                costPrice = (price.toDoubleOrNull() ?: 0.0) * 0.8, // Estimate cost
                stockQuantity = stock.toIntOrNull() ?: 0,
                categoryId = null,
                supplierId = null,
                imageUrl = null
            )
            
            if (id != null && id > 0) {
                productRepository.updateProduct(entity)
            } else {
                productRepository.insertProduct(entity)
            }
        }
    }
}

data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false
)
