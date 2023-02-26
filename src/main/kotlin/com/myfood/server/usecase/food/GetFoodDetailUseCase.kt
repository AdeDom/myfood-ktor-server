package com.myfood.server.usecase.food

import com.myfood.server.data.models.response.FoodDetailResponse
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.utility.exception.ApplicationException

internal class GetFoodDetailUseCase(
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(foodId: String?): FoodDetailResponse {
        return when {
            foodId.isNullOrBlank() -> throw ApplicationException("Food id is null or blank.")
            foodId.toIntOrNull() == null -> throw ApplicationException("Food id is text.")
            else -> foodRepository.getFoodDetail(foodId.toInt())
        }
    }
}