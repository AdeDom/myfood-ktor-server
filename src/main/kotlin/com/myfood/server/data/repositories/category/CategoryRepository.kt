package com.myfood.server.data.repositories.category

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.models.response.CategoryResponse
import com.myfood.server.data.repositories.Resource

interface CategoryRepository {

    suspend fun findCategoryId(categoryId: Int): Long

    suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): Resource<BaseResponse<String>>

    suspend fun getCategoryAll(): Resource<BaseResponse<List<CategoryResponse>>>

    suspend fun findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId: Int): Int
}