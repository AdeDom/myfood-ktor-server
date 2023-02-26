package com.myfood.server.usecase.category

import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.utility.exception.ApplicationException

internal class InsertCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(insertCategoryRequest: InsertCategoryRequest): String {
        val (categoryName, image) = insertCategoryRequest
        return when {
            categoryName.isNullOrBlank() -> throw ApplicationException("Category name is null or blank.")
            image.isNullOrBlank() -> throw ApplicationException("Image is null or blank.")
            else -> categoryRepository.insertCategory(insertCategoryRequest)
        }
    }
}