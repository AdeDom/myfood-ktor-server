package com.myfood.server.data.resouce.local.category

import com.myfood.server.data.models.entities.CategoryEntity
import com.myfood.server.utility.constant.AppConstant

internal class CategoryLocalDataSourceImpl : CategoryLocalDataSource {

    private val categoryList = mutableListOf<CategoryEntity>()

    override suspend fun getCategoryAll(): List<CategoryEntity> {
        return this.categoryList
    }

    override suspend fun insertCategoryAll(categoryList: List<CategoryEntity>): Int {
        this.categoryList.addAll(categoryList)
        return this.categoryList.size
    }

    override suspend fun deleteCategoryAll() {
        this.categoryList.clear()
    }

    override suspend fun findCategoryTypeCountByCategoryIdAndCategoryTypeRecommend(categoryId: Int): Int {
        return this.categoryList
            .filter { categoryEntity ->
                categoryEntity.categoryId == categoryId
            }
            .filter { categoryEntity ->
                categoryEntity.categoryTypeName == AppConstant.CATEGORY_TYPE_RECOMMEND
            }
            .size
    }
}