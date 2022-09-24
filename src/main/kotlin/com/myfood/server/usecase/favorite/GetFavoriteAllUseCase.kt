package com.myfood.server.usecase.favorite

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.FavoriteResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.favorite.FavoriteRepository

internal class GetFavoriteAllUseCase(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<List<FavoriteResponse>>> {
        return when {
            else -> {
                favoriteRepository.getFavoriteAll()
            }
        }
    }
}