package com.kutub.nexora.erp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.model.SupplierEntity
import com.kutub.nexora.erp.data.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {

    private val _suppliers = MutableStateFlow<List<SupplierEntity>>(emptyList())
    val suppliers: StateFlow<List<SupplierEntity>> = _suppliers.asStateFlow()

    init {
        viewModelScope.launch {
            supplierRepository.getAllSuppliers().collect {
                _suppliers.value = it
            }
        }
    }

    fun insertSupplier(name: String, contactName: String?, phone: String, email: String?, address: String?, status: Boolean) {
        viewModelScope.launch {
            val newSupplier = SupplierEntity(
                name = name,
                contactName = contactName,
                phone = phone,
                email = email,
                address = address,
                status = status
            )
            supplierRepository.insertSupplier(newSupplier)
        }
    }

    fun updateSupplier(supplier: SupplierEntity) {
        viewModelScope.launch {
            supplierRepository.updateSupplier(supplier)
        }
    }

    fun deleteSupplier(supplier: SupplierEntity) {
        viewModelScope.launch {
            supplierRepository.deleteSupplier(supplier)
        }
    }

    suspend fun getSupplierById(id: Long): SupplierEntity? {
        return supplierRepository.getSupplierById(id)
    }
}
