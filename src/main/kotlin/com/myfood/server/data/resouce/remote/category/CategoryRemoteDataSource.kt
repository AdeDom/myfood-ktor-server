package com.myfood.server.data.resouce.remote.category

import com.myfood.server.data.models.entities.CategoryEntity
import com.myfood.server.data.models.request.InsertCategoryRequest

interface CategoryRemoteDataSource {

    suspend fun findCategoryId(categoryId: Int): Long

    suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): Int?

    suspend fun getCategoryAll(): List<CategoryEntity>
}