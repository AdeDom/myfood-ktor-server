package com.myfood.server.usecase.category

import com.myfood.server.data.models.response.CategoryResponse
import com.myfood.server.data.repositories.category.CategoryRepository

internal class GetCategoryAllUseCase(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(): List<CategoryResponse> {
        return categoryRepository.getCategoryAll()
    }
}