package com.kutub.nexora.erp.data.repository

import com.kutub.nexora.erp.data.local.dao.CategoryDao
import com.kutub.nexora.erp.data.model.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    
    override suspend fun getCategoryById(id: Long): CategoryEntity? = categoryDao.getCategoryById(id)
    
    override suspend fun insertCategory(category: CategoryEntity): Long = categoryDao.insertCategory(category)
    
    override suspend fun updateCategory(category: CategoryEntity) = categoryDao.updateCategory(category)
    
    override suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)
    
    override fun getCategoryCount(): Flow<Int> = categoryDao.getCategoryCount()
}
