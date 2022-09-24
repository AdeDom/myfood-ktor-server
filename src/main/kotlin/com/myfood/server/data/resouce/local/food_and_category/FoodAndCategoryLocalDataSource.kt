package com.myfood.server.data.resouce.local.food_and_category

import com.myfood.server.data.models.entities.FoodAndCategoryEntity

internal interface FoodAndCategoryLocalDataSource {

    suspend fun getFoodAndCategoryAll(): List<FoodAndCategoryEntity>

    suspend fun insertFoodAndCategoryAll(foodAndCategoryAll: List<FoodAndCategoryEntity>): Int

    suspend fun deleteFoodAndCategoryAll()
}