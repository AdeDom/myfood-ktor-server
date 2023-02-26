package com.myfood.server.usecase.food

import com.myfood.server.data.models.response.FoodDetailResponse
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.utility.exception.ApplicationException

internal class GetFoodByCategoryIdUseCase(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(categoryId: String?): List<FoodDetailResponse> {
        return when {
            categoryId.isNullOrBlank() -> throw ApplicationException("Category id is null or blank.")
            categoryId.toIntOrNull() == null -> throw ApplicationException("Category id is text.")
            else -> {
                val isCategoryTypeRecommend = isCategoryTypeRecommend(categoryId.toInt())
                if (isCategoryTypeRecommend) {
                    return foodRepository.getFoodAndCategoryAll()
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
                } else {
                    return foodRepository.getFoodByCategoryId(categoryId.toInt())
                }
            }
        }
    }

    private suspend fun isCategoryTypeRecommend(categoryId: Int): Boolean {
        return categoryRepository.findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId) == 1
    }
}