package com.myfood.server.usecase.food

import com.myfood.server.data.models.entities.MyFoodEntity
import com.myfood.server.data.repositories.food.FoodRepository

internal class MyFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(): List<MyFoodEntity> {
        return foodRepository.getMyFood()
    }
}