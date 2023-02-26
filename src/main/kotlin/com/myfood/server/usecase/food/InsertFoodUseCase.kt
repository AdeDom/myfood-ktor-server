package com.myfood.server.usecase.food

import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.utility.exception.ApplicationException

internal class InsertFoodUseCase(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(insertFoodRequest: InsertFoodRequest): String {
        val (foodName, _, image, price, _, categoryId) = insertFoodRequest
        return when {
            foodName.isNullOrBlank() -> throw ApplicationException("Food name is null or blank.")
            image.isNullOrBlank() -> throw ApplicationException("Image is null or blank.")
            price == null -> throw ApplicationException("Price is null or blank.")
            categoryId == null -> throw ApplicationException("Category id is null or blank.")
            hasCategory(categoryId) -> throw ApplicationException("Category id not found.")
            else -> foodRepository.insertFood(insertFoodRequest)
        }
    }

    private suspend fun hasCategory(categoryId: Int): Boolean {
        val isFindCategoryId = categoryRepository.findCategoryId(categoryId)
        return isFindCategoryId == 0L
    }
}