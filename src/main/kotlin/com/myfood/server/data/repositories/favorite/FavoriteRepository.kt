package com.myfood.server.data.repositories.favorite

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.FavoriteResponse
import com.myfood.server.data.models.web_sockets.FavoriteWebSocketsResponse
import com.myfood.server.data.repositories.Resource

internal interface FavoriteRepository {

    suspend fun getFavoriteAll(): Resource<BaseResponse<List<FavoriteResponse>>>

    suspend fun myFavorite(userId: String, foodId: Int): Resource<BaseResponse<FavoriteWebSocketsResponse>>

    suspend fun deleteFavoriteAll(): Resource<BaseResponse<String>>

    suspend fun syncDataFavorite(): Resource<BaseResponse<String>>
}