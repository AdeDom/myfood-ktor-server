package com.myfood.server.data.resouce.local.food_and_category

import com.myfood.server.data.models.entities.FoodAndCategoryEntity

internal class FoodAndCategoryLocalDataSourceImpl : FoodAndCategoryLocalDataSource {

    private val foodAndCategoryList = mutableListOf<FoodAndCategoryEntity>()

    override suspend fun getFoodAndCategoryAll(): List<FoodAndCategoryEntity> {
        return this.foodAndCategoryList
    }

    override suspend fun insertFoodAndCategoryAll(foodAndCategoryAll: List<FoodAndCategoryEntity>): Int {
        val list = foodAndCategoryAll.map { foodAndCategoryAllEntity ->
            FoodAndCategoryEntity(
                foodAndCategoryId = System.currentTimeMillis().toInt(),
                foodId = foodAndCategoryAllEntity.foodId,
                foodName = foodAndCategoryAllEntity.foodName,
                alias = foodAndCategoryAllEntity.alias,
                foodImage = foodAndCategoryAllEntity.foodImage,
                price = foodAndCategoryAllEntity.price,
                description = foodAndCategoryAllEntity.description,
                status = foodAndCategoryAllEntity.status,
                foodCreated = foodAndCategoryAllEntity.foodCreated,
                foodUpdated = foodAndCategoryAllEntity.foodUpdated,
                categoryId = foodAndCategoryAllEntity.categoryId,
                categoryName = foodAndCategoryAllEntity.categoryName,
                categoryImage = foodAndCategoryAllEntity.categoryImage,
                categoryTypeName = foodAndCategoryAllEntity.categoryTypeName,
                categoryCreated = foodAndCategoryAllEntity.categoryCreated,
                categoryUpdated = foodAndCategoryAllEntity.categoryUpdated,
            )
        }
        this.foodAndCategoryList.addAll(list)
        return this.foodAndCategoryList.size
    }

    override suspend fun deleteFoodAndCategoryAll() {
        this.foodAndCategoryList.clear()
    }
}