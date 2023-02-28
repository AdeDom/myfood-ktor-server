package com.myfood.server.data.repositories.food

import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.data.models.response.FoodAndCategoryResponse
import com.myfood.server.data.models.response.FoodDetailResponse

internal interface FoodRepository {

    suspend fun insertFood(insertFoodRequest: InsertFoodRequest): String

    suspend fun getFoodDetail(foodId: Int): FoodDetailResponse

    suspend fun getFoodByCategoryId(categoryId: Int): List<FoodDetailResponse>

    suspend fun getFoodAndCategoryAll(): List<FoodAndCategoryResponse>
}