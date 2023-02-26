package com.myfood.server.data.repositories.favorite

import com.myfood.server.data.models.response.FavoriteResponse
import com.myfood.server.data.models.web_sockets.FavoriteWebSocketsResponse

internal interface FavoriteRepository {

    suspend fun getFavoriteAll(): List<FavoriteResponse>

    suspend fun myFavorite(userId: String, foodId: Int): FavoriteWebSocketsResponse

    suspend fun deleteFavoriteAll(): String

    suspend fun syncDataFavorite(): String
}