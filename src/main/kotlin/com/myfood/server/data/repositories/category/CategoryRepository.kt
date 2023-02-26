package com.myfood.server.data.repositories.category

import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.models.response.CategoryResponse

internal interface CategoryRepository {

    suspend fun findCategoryId(categoryId: Int): Long

    suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): String

    suspend fun getCategoryAll(): List<CategoryResponse>

    suspend fun findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId: Int): Int
}