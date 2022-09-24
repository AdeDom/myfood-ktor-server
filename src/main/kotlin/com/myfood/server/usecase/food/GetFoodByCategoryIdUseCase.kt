package com.myfood.server.usecase.food

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.FoodDetailResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.utility.constant.ResponseKeyConstant

class GetFoodByCategoryIdUseCase(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(categoryId: String?): Resource<BaseResponse<List<FoodDetailResponse>>> {
        val response = BaseResponse<List<FoodDetailResponse>>()

        return when {
            categoryId.isNullOrBlank() -> {
                response.error = BaseError(message = "Category id is null or blank.")
                Resource.Error(response)
            }

            categoryId.toIntOrNull() == null -> {
                response.error = BaseError(message = "Category id is text.")
                Resource.Error(response)
            }

            else -> {
                val isCategoryTypeRecommend = isCategoryTypeRecommend(categoryId.toInt())
                if (isCategoryTypeRecommend) {
                    val getFoodAndCategoryAll = foodRepository.getFoodAndCategoryAll()
                    val foodListResponse = getFoodAndCategoryAll
                        .asSequence()
                        .filter {
                            (it.favorite ?: 0L) > 0 || (it.ratingScore ?: 0F) > 0
                        }
                        .map {
                            FoodDetailResponse(
                                foodId = it.foodId ?: 0,
                                foodName = it.foodName.orEmpty(),
                                alias = it.alias,
                                image = it.foodImage.orEmpty(),
                                price = it.price ?: 0.0,
                                description = it.description,
                                favorite = it.favorite ?: 0,
                                ratingScore = it.ratingScore,
                                ratingScoreCount = it.ratingScoreCount,
                                categoryId = it.categoryId,
                                status = it.status.orEmpty(),
                                created = it.foodCreated.orEmpty(),
                                updated = it.foodUpdated,
                            )
                        }
                        .sortedByDescending {
                            it.ratingScore
                        }
                        .sortedByDescending {
                            it.favorite
                        }
                        .toList()
                    response.status = ResponseKeyConstant.SUCCESS
                    response.result = foodListResponse
                    return Resource.Success(response)
                } else {
                    val foodListResponse = foodRepository.getFoodByCategoryId(categoryId.toInt())
                    response.status = ResponseKeyConstant.SUCCESS
                    response.result = foodListResponse
                    return Resource.Success(response)
                }
            }
        }
    }

    private suspend fun isCategoryTypeRecommend(categoryId: Int): Boolean {
        return categoryRepository.findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId) == 1
    }
}