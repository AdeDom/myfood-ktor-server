package com.myfood.server.data.models.response

@kotlinx.serialization.Serializable
data class RatingScoreResponse(
    val ratingScoreId: String,
    val userId: String,
    val foodId: Int,
    val ratingScore: Float,
    val isBackup: Boolean,
    val created: String,
    val updated: String?,
)