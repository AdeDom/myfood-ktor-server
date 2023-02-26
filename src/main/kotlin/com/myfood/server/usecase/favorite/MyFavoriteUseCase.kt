package com.myfood.server.usecase.favorite

import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.data.models.web_sockets.FavoriteWebSocketsResponse
import com.myfood.server.data.repositories.favorite.FavoriteRepository
import com.myfood.server.usecase.auth.TokenUseCase
import com.myfood.server.utility.exception.ApplicationException

internal class MyFavoriteUseCase(
    private val tokenUseCase: TokenUseCase,
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(authKey: String?, myFavoriteRequest: MyFavoriteRequest): FavoriteWebSocketsResponse {
        val (foodId) = myFavoriteRequest
        return when {
            tokenUseCase.isValidateToken(authKey) -> throw ApplicationException(tokenUseCase.getBaseError(authKey))
            foodId == null -> throw ApplicationException("Food id is null.")
            else -> favoriteRepository.myFavorite(tokenUseCase.getUserId(authKey), foodId)
        }
    }
}