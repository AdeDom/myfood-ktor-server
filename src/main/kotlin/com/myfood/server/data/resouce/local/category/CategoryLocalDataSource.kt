package com.myfood.server.data.resouce.local.category

import com.myfood.server.data.models.entities.CategoryEntity

interface CategoryLocalDataSource {

    suspend fun getCategoryAll(): List<CategoryEntity>

    suspend fun insertCategoryAll(categoryList: List<CategoryEntity>): Int

    suspend fun deleteCategoryAll()

    suspend fun findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId: Int): Int
}