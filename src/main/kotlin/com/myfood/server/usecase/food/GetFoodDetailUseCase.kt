package com.myfood.server.usecase.food

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.FoodDetailResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.food.FoodRepository

internal class GetFoodDetailUseCase(
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(foodId: String?): Resource<BaseResponse<FoodDetailResponse>> {
        val response = BaseResponse<FoodDetailResponse>()

        return when {
            foodId.isNullOrBlank() -> {
                response.error = BaseError(message = "Food id is null or blank.")
                Resource.Error(response)
            }

            foodId.toIntOrNull() == null -> {
                response.error = BaseError(message = "Food id is text.")
                Resource.Error(response)
            }

            else -> {
                foodRepository.getFoodDetail(foodId.toInt())
            }
        }
    }
}