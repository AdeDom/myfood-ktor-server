package com.myfood.server.data.resouce.local.favorite

import com.myfood.server.data.models.entities.FavoriteEntity

interface FavoriteLocalDataSource {

    suspend fun getFavoriteAll(): List<FavoriteEntity>

    suspend fun replaceFavorite(
        favoriteId: String,
        userId: String,
        foodId: Int,
        isFavorite: Int,
        isBackup: Int,
        created: String,
        updated: String?,
    ): Int?

    suspend fun deleteFavoriteAll(): Int

    suspend fun findFavoriteEntityByUserIdAndFoodId(userId: String, foodId: Int): FavoriteEntity?

    suspend fun getFavoriteListByBackupIsLocal(): List<FavoriteEntity>

    suspend fun updateFavoriteByBackupIsRemote(): Int

    suspend fun replaceFavoriteAll(favoriteList: List<FavoriteEntity>): Int

    suspend fun getFavoriteCountByFoodIdAndFavorite(foodId: Int): Long
}