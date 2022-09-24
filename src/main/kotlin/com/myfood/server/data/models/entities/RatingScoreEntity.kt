package com.myfood.server.data.models.entities

data class RatingScoreEntity(
    val ratingScoreId: String,
    val userId: String,
    val foodId: Int,
    val ratingScore: Float,
    val isBackup: Int,
    val created: String,
    val updated: String?,
)