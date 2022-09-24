package com.myfood.server.data.repositories.category

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.models.response.CategoryResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.resouce.local.category.CategoryLocalDataSource
import com.myfood.server.data.resouce.remote.category.CategoryRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.constant.ResponseKeyConstant

class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
) : CategoryRepository {

    override suspend fun findCategoryId(categoryId: Int): Long {
        return categoryRemoteDataSource.findCategoryId(categoryId)
    }

    override suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val isInsertCategory = categoryRemoteDataSource.insertCategory(insertCategoryRequest) == 1
        return if (isInsertCategory) {
            response.status = ResponseKeyConstant.SUCCESS
            response.result = "Insert category success."
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "Insert category failed.")
            Resource.Error(response)
        }
    }

    override suspend fun getCategoryAll(): Resource<BaseResponse<List<CategoryResponse>>> {
        val response = BaseResponse<List<CategoryResponse>>()

        var getCategoryAll = categoryLocalDataSource.getCategoryAll()
        if (getCategoryAll.isEmpty()) {
            getCategoryAll = categoryRemoteDataSource.getCategoryAll()

            categoryLocalDataSource.deleteCategoryAll()
            val listLocalCount = categoryLocalDataSource.insertCategoryAll(getCategoryAll)
            if (listLocalCount != getCategoryAll.size) {
                categoryLocalDataSource.deleteCategoryAll()
            }
        }

        val getCategoryAllResponse = getCategoryAll.map { categoryEntity ->
            CategoryResponse(
                categoryId = categoryEntity.categoryId,
                categoryName = categoryEntity.categoryName,
                image = categoryEntity.image,
                categoryTypeName = categoryEntity.categoryTypeName,
                created = categoryEntity.created.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
                updated = categoryEntity.updated?.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
            )
        }
        response.status = ResponseKeyConstant.SUCCESS
        response.result = getCategoryAllResponse
        return Resource.Success(response)
    }

    override suspend fun findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId: Int): Int {
        var getCategoryAll = categoryLocalDataSource.getCategoryAll()
        if (getCategoryAll.isEmpty()) {
            getCategoryAll = categoryRemoteDataSource.getCategoryAll()

            categoryLocalDataSource.deleteCategoryAll()
            val listLocalCount = categoryLocalDataSource.insertCategoryAll(getCategoryAll)
            if (listLocalCount != getCategoryAll.size) {
                categoryLocalDataSource.deleteCategoryAll()
            }
        }

        return categoryLocalDataSource.findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId)
    }
}