package com.myfood.server.data.resouce.remote.food

import com.myfood.server.data.models.entities.MyFoodEntity

interface MyFoodRemoteDataSource {

    suspend fun getMyFood(): List<MyFoodEntity>
}