package com.kutub.nexora.erp.ui.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.model.ProductEntity
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.model.SaleItemEntity
import com.kutub.nexora.erp.data.local.PreferencesManager
import com.kutub.nexora.erp.data.repository.ProductRepository
import com.kutub.nexora.erp.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
)

@HiltViewModel
class PosViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val currency = preferencesManager.currencyFlow

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    init {
        viewModelScope.launch {
            productRepository.getAllProducts().collect {
                _products.value = it
            }
        }
    }

    fun addToCart(product: ProductEntity) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            if (existingItem.quantity < product.stockQuantity) {
                currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
            }
        } else {
            if (product.stockQuantity > 0) {
                currentCart.add(CartItem(product, 1))
            }
        }
        
        _cartItems.value = currentCart
        calculateTotal()
    }

    fun removeFromCart(product: ProductEntity) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            if (existingItem.quantity > 1) {
                currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentCart.removeAt(existingItemIndex)
            }
        }
        
        _cartItems.value = currentCart
        calculateTotal()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        calculateTotal()
    }

    private fun calculateTotal() {
        val total = _cartItems.value.sumOf { it.product.price * it.quantity }
        _totalAmount.value = total
    }

    fun checkout(customerName: String, discount: Double, onComplete: (SaleEntity, List<SaleItemEntity>) -> Unit) {
        val currentCart = _cartItems.value
        if (currentCart.isEmpty()) return

        val total = currentCart.sumOf { it.product.price * it.quantity }
        val finalAmount = (total - discount).coerceAtLeast(0.0)

        viewModelScope.launch {
            val sale = SaleEntity(
                customerName = customerName.takeIf { it.isNotBlank() },
                totalAmount = total,
                discount = discount,
                finalAmount = finalAmount
            )

            val saleItems = currentCart.map { cartItem ->
                SaleItemEntity(
                    saleId = 0, // Will be set by repository
                    productId = cartItem.product.id,
                    productName = cartItem.product.name,
                    quantity = cartItem.quantity,
                    unitPrice = cartItem.product.price,
                    totalPrice = cartItem.product.price * cartItem.quantity
                )
            }

            val saleId = saleRepository.completeSale(sale, saleItems)
            clearCart()
            
            // Pass the completed data back to the UI for the receipt
            val completedSale = sale.copy(id = saleId)
            val completedItems = saleItems.map { it.copy(saleId = saleId) }
            onComplete(completedSale, completedItems)
        }
    }
}
