package com.myfood.server.data.models.entities

data class FavoriteEntity(
    val favoriteId: String,
    val userId: String,
    val foodId: Int,
    val isFavorite: Int,
    val isBackup: Int,
    val created: String,
    val updated: String?,
)