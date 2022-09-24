package com.myfood.server.data.repositories.food

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.entities.MyFoodEntity
import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.data.models.response.FoodAndCategoryResponse
import com.myfood.server.data.models.response.FoodDetailResponse
import com.myfood.server.data.repositories.Resource

interface FoodRepository {

    suspend fun getMyFood(): Resource<BaseResponse<List<MyFoodEntity>>>

    suspend fun insertFood(insertFoodRequest: InsertFoodRequest): Resource<BaseResponse<String>>

    suspend fun getFoodDetail(foodId: Int): Resource<BaseResponse<FoodDetailResponse>>

    suspend fun getFoodByCategoryId(categoryId: Int): List<FoodDetailResponse>

    suspend fun getFoodAndCategoryAll(): List<FoodAndCategoryResponse>
}