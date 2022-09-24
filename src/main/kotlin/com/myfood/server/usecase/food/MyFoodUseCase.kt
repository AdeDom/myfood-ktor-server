package com.myfood.server.usecase.food

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.entities.MyFoodEntity
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.food.FoodRepository

internal class MyFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<List<MyFoodEntity>>> {
        return when {
            else -> {
                foodRepository.getMyFood()
            }
        }
    }
}