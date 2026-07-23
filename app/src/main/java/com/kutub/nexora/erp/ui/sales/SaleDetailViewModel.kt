package com.kutub.nexora.erp.ui.sales

import androidx.lifecycle.ViewModel
import com.kutub.nexora.erp.data.local.PreferencesManager
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.model.SaleItemEntity
import com.kutub.nexora.erp.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class SaleDetailViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val currency = preferencesManager.currencyFlow

    suspend fun getSaleDetails(saleId: Long): Pair<SaleEntity?, List<SaleItemEntity>> {
        val allSales = saleRepository.getAllSales().firstOrNull() ?: emptyList()
        val sale = allSales.find { it.id == saleId }
        val saleItems = saleRepository.getSaleItemsBySaleId(saleId)
        return Pair(sale, saleItems)
    }
}
