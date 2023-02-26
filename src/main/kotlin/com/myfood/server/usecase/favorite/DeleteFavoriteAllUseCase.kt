package com.myfood.server.usecase.favorite

import com.myfood.server.data.repositories.favorite.FavoriteRepository

internal class DeleteFavoriteAllUseCase(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(): String {
        return favoriteRepository.deleteFavoriteAll()
    }
}