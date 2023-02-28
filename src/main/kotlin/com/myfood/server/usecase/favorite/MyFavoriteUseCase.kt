package com.myfood.server.usecase.favorite

import com.auth0.jwt.JWT
import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.data.models.web_sockets.FavoriteWebSocketsResponse
import com.myfood.server.data.repositories.favorite.FavoriteRepository
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.jwt.JwtHelper

internal class MyFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(authKey: String?, myFavoriteRequest: MyFavoriteRequest): FavoriteWebSocketsResponse {
        val (foodId) = myFavoriteRequest
        return when (foodId) {
            null -> throw ApplicationException("Food id is null.")
            else -> {
                val accessToken = authKey?.replace("Bearer", "")?.trim()
                val userId = JWT().decodeJwt(accessToken).getClaim(JwtHelper.USER_ID).asString()
                favoriteRepository.myFavorite(userId, foodId)
            }
        }
    }
}