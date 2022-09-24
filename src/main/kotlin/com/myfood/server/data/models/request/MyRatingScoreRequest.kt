package com.myfood.server.data.models.request

@kotlinx.serialization.Serializable
data class MyRatingScoreRequest(
    val foodId: Int?,
    val ratingScore: Float?,
)