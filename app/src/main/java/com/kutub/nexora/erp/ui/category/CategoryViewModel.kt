package com.kutub.nexora.erp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.nexora.erp.data.model.CategoryEntity
import com.kutub.nexora.erp.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {
                _categories.value = it
            }
        }
    }

    fun insertCategory(name: String, description: String?) {
        viewModelScope.launch {
            val newCategory = CategoryEntity(name = name, description = description)
            categoryRepository.insertCategory(newCategory)
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }

    suspend fun getCategoryById(id: Long): CategoryEntity? {
        return categoryRepository.getCategoryById(id)
    }
}
