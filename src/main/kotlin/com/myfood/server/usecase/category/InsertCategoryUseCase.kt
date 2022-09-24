package com.myfood.server.usecase.category

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.category.CategoryRepository

class InsertCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(insertCategoryRequest: InsertCategoryRequest): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val (categoryName, image) = insertCategoryRequest
        return when {
            categoryName.isNullOrBlank() -> {
                response.error = BaseError(message = "Category name is null or blank.")
                Resource.Error(response)
            }

            image.isNullOrBlank() -> {
                response.error = BaseError(message = "Image is null or blank.")
                Resource.Error(response)
            }

            else -> {
                categoryRepository.insertCategory(insertCategoryRequest)
            }
        }
    }
}