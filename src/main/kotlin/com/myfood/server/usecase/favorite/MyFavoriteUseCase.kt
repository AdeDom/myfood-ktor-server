package com.myfood.server.usecase.favorite

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.data.models.web_sockets.FavoriteWebSocketsResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.favorite.FavoriteRepository
import com.myfood.server.usecase.auth.TokenUseCase

internal class MyFavoriteUseCase(
    private val tokenUseCase: TokenUseCase,
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(
        authKey: String?,
        myFavoriteRequest: MyFavoriteRequest
    ): Resource<BaseResponse<FavoriteWebSocketsResponse>> {
        val response = BaseResponse<FavoriteWebSocketsResponse>()

        val (foodId) = myFavoriteRequest
        return when {
            tokenUseCase.isValidateToken(authKey) -> {
                response.error = tokenUseCase.getBaseError(authKey)
                Resource.Error(response)
            }

            foodId == null -> {
                response.error = BaseError(message = "Food id is null.")
                Resource.Error(response)
            }

            else -> {
                val userId = tokenUseCase.getUserId(authKey)
                favoriteRepository.myFavorite(userId, foodId)
            }
        }
    }
}