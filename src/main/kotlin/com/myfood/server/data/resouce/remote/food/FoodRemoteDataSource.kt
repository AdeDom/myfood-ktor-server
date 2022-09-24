package com.myfood.server.data.resouce.remote.food

import com.myfood.server.data.models.entities.FoodAndCategoryEntity
import com.myfood.server.data.models.entities.FoodEntity
import com.myfood.server.data.models.request.InsertFoodRequest

internal interface FoodRemoteDataSource {

    suspend fun insertFood(insertFoodRequest: InsertFoodRequest, status: String): Int?

    suspend fun getFoodDetail(foodId: Int): FoodEntity?

    suspend fun getFoodByCategoryId(categoryId: Int): List<FoodEntity>

    suspend fun getFoodAndCategoryAll(): List<FoodAndCategoryEntity>

    suspend fun getFoodAll(): List<FoodEntity>
}