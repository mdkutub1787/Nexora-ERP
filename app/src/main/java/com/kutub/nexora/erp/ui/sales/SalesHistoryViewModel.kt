package com.kutub.nexora.erp.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.kutub.nexora.erp.data.local.PreferencesManager

@HiltViewModel
class SalesHistoryViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val currency = preferencesManager.currencyFlow

    private val _sales = MutableStateFlow<List<SaleEntity>>(emptyList())
    val sales: StateFlow<List<SaleEntity>> = _sales.asStateFlow()

    init {
        loadSales()
    }

    private fun loadSales() {
        viewModelScope.launch {
            saleRepository.getAllSales()
                .catch { e -> 
                    // Handle error
                }
                .collect { saleList ->
                    // Sort by newest first
                    _sales.value = saleList.sortedByDescending { it.saleDate }
                }
        }
    }
}
