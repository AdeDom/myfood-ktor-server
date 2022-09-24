package com.myfood.server.data.models.response

@kotlinx.serialization.Serializable
data class FoodDetailResponse(
    val foodId: Int,
    val foodName: String,
    val alias: String?,
    val image: String,
    val price: Double,
    val description: String?,
    val favorite: Long?,
    val ratingScore: Float?,
    val ratingScoreCount: String?,
    val categoryId: Int,
    val status: String,
    val created: String,
    val updated: String?,
)