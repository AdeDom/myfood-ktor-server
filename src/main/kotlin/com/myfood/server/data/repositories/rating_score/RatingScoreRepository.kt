package com.myfood.server.data.repositories.rating_score

import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse

internal interface RatingScoreRepository {

    suspend fun getRatingScoreAll(): List<RatingScoreResponse>

    suspend fun myRatingScore(userId: String, foodId: Int, ratingScore: Float): RatingScoreWebSocketsResponse

    suspend fun deleteRatingScoreAll(): String

    suspend fun syncDataRatingScore(): String
}