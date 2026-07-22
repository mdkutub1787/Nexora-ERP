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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

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

    // Temporary method for mock data insertion during testing
    fun insertMockProduct() {
        viewModelScope.launch {
            productRepository.insertProduct(
                ProductEntity(
                    name = "Mock Product " + (1000..9999).random(),
                    barcode = "BAR" + (100000..999999).random(),
                    sku = "SKU" + (100..999).random(),
                    price = (100..1000).random().toDouble(),
                    costPrice = (50..800).random().toDouble(),
                    stockQuantity = (10..100).random(),
                    categoryId = null,
                    supplierId = null,
                    imageUrl = null
                )
            )
        }
    }
}

data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false
)
