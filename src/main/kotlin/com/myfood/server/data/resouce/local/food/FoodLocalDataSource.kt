package com.myfood.server.data.resouce.local.food

import com.myfood.server.data.models.entities.FoodEntity

interface FoodLocalDataSource {

    suspend fun getFoodDetail(foodId: Int): FoodEntity?

    suspend fun getFoodByCategoryId(categoryId: Int): List<FoodEntity>

    suspend fun getFoodAll(): List<FoodEntity>

    suspend fun insertFoodAll(foodList: List<FoodEntity>): Int

    suspend fun deleteFoodAll()
}