package com.myfood.server.usecase.food

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.CategoryFoodDetailResponse
import com.myfood.server.data.models.response.FoodAndCategoryGroupResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.utility.constant.ResponseKeyConstant

internal class GetFoodAndCategoryGroupAllUseCase(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<List<FoodAndCategoryGroupResponse>>> {
        val response = BaseResponse<List<FoodAndCategoryGroupResponse>>()

        val getFoodAndCategoryAll = foodRepository.getFoodAndCategoryAll()

        val foodAndCategoryGroupResponse = getFoodAndCategoryAll
            .distinctBy { foodAndCategory ->
                foodAndCategory.categoryId
            }
            .map { foodAndCategory ->
                val isCategoryTypeRecommend = isCategoryTypeRecommend(foodAndCategory.categoryId)
                if (isCategoryTypeRecommend) {
                    val foodDetailList = getFoodAndCategoryAll
                        .asSequence()
                        .filter {
                            (it.favorite ?: 0L) > 0 || (it.ratingScore ?: 0F) > 0
                        }
                        .map {
                            CategoryFoodDetailResponse(
                                foodId = it.foodId,
                                foodName = it.foodName,
                                alias = it.alias,
                                image = it.foodImage,
                                price = it.price,
                                description = it.description,
                                favorite = it.favorite,
                                ratingScore = it.ratingScore,
                                ratingScoreCount = it.ratingScoreCount,
                                categoryId = it.categoryId,
                                status = it.status,
                                created = it.foodCreated,
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

                    FoodAndCategoryGroupResponse(
                        categoryId = foodAndCategory.categoryId,
                        categoryName = foodAndCategory.categoryName,
                        image = foodAndCategory.categoryImage,
                        categoryTypeName = foodAndCategory.categoryTypeName,
                        created = foodAndCategory.categoryCreated,
                        updated = foodAndCategory.categoryUpdated,
                        foodDetailList = foodDetailList,
                    )
                } else {
                    val foodDetailList = getFoodAndCategoryAll
                        .asSequence()
                        .filter {
                            it.categoryId == foodAndCategory.categoryId
                        }
                        .filter {
                            it.foodId != null
                        }
                        .map {
                            CategoryFoodDetailResponse(
                                foodId = it.foodId,
                                foodName = it.foodName,
                                alias = it.alias,
                                image = it.foodImage,
                                price = it.price,
                                description = it.description,
                                favorite = it.favorite,
                                ratingScore = it.ratingScore,
                                ratingScoreCount = it.ratingScoreCount,
                                categoryId = it.categoryId,
                                status = it.status,
                                created = it.foodCreated,
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

                    FoodAndCategoryGroupResponse(
                        categoryId = foodAndCategory.categoryId,
                        categoryName = foodAndCategory.categoryName,
                        image = foodAndCategory.categoryImage,
                        categoryTypeName = foodAndCategory.categoryTypeName,
                        created = foodAndCategory.categoryCreated,
                        updated = foodAndCategory.categoryUpdated,
                        foodDetailList = foodDetailList,
                    )
                }
            }

        response.status = ResponseKeyConstant.SUCCESS
        response.result = foodAndCategoryGroupResponse
        return Resource.Success(response)
    }

    private suspend fun isCategoryTypeRecommend(categoryId: Int): Boolean {
        return categoryRepository.findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId) == 1
    }
}