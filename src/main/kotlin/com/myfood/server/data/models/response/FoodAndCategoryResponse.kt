package com.myfood.server.data.models.response

@kotlinx.serialization.Serializable
data class FoodAndCategoryResponse(
    val foodAndCategoryId: Int,
    val foodId: Int?,
    val foodName: String?,
    val alias: String?,
    val foodImage: String?,
    val price: Double?,
    val description: String?,
    val favorite: Long?,
    val ratingScore: Float?,
    val ratingScoreCount: String?,
    val status: String?,
    val foodCreated: String?,
    val foodUpdated: String?,
    val categoryId: Int,
    val categoryName: String,
    val categoryImage: String,
    val categoryTypeName: String,
    val categoryCreated: String,
    val categoryUpdated: String?,
)