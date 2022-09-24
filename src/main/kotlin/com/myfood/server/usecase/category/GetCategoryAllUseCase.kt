package com.myfood.server.usecase.category

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.CategoryResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.category.CategoryRepository

internal class GetCategoryAllUseCase(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<List<CategoryResponse>>> {
        return when {
            else -> {
                categoryRepository.getCategoryAll()
            }
        }
    }
}