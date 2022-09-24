package com.myfood.server.data.resouce.remote.favorite

import com.myfood.server.data.models.entities.FavoriteEntity

interface FavoriteRemoteDataSource {

    suspend fun replaceFavoriteAll(favoriteList: List<FavoriteEntity>): Int

    suspend fun getFavoriteAll(): List<FavoriteEntity>
}