package com.myfood.server.data.models.response

@kotlinx.serialization.Serializable
data class FavoriteResponse(
    val favoriteId: String,
    val userId: String,
    val foodId: Int,
    val isFavorite: Boolean,
    val isBackup: Boolean,
    val created: String,
    val updated: String?,
)