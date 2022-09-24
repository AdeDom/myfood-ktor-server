package com.myfood.server.usecase.favorite

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.favorite.FavoriteRepository

class DeleteFavoriteAllUseCase(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<String>> {
        return when {
            else -> {
                favoriteRepository.deleteFavoriteAll()
            }
        }
    }
}