package com.myfood.server.data.repositories.category

import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.models.response.CategoryResponse
import com.myfood.server.data.resouce.local.category.CategoryLocalDataSource
import com.myfood.server.data.resouce.remote.category.CategoryRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.exception.ApplicationException

internal class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
) : CategoryRepository {

    override suspend fun findCategoryId(categoryId: Int): Long {
        return categoryRemoteDataSource.findCategoryId(categoryId)
    }

    override suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): String {
        val isInsertCategory = categoryRemoteDataSource.insertCategory(insertCategoryRequest) == 1
        return if (isInsertCategory) {
            "Insert category success."
        } else {
            throw ApplicationException("Insert category failed.")
        }
    }

    override suspend fun getCategoryAll(): List<CategoryResponse> {
        var getCategoryAll = categoryLocalDataSource.getCategoryAll()
        if (getCategoryAll.isEmpty()) {
            getCategoryAll = categoryRemoteDataSource.getCategoryAll()

            categoryLocalDataSource.deleteCategoryAll()
            val listLocalCount = categoryLocalDataSource.insertCategoryAll(getCategoryAll)
            if (listLocalCount != getCategoryAll.size) {
                categoryLocalDataSource.deleteCategoryAll()
            }
        }

        return getCategoryAll.map { categoryEntity ->
            CategoryResponse(
                categoryId = categoryEntity.categoryId,
                categoryName = categoryEntity.categoryName,
                image = categoryEntity.image,
                categoryTypeName = categoryEntity.categoryTypeName,
                created = categoryEntity.created.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
                updated = categoryEntity.updated?.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
            )
        }
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