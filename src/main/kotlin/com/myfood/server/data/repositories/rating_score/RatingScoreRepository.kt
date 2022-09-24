package com.myfood.server.data.repositories.rating_score

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.repositories.Resource

internal interface RatingScoreRepository {

    suspend fun getRatingScoreAll(): Resource<BaseResponse<List<RatingScoreResponse>>>

    suspend fun myRatingScore(
        userId: String,
        foodId: Int,
        ratingScore: Float
    ): Resource<BaseResponse<RatingScoreWebSocketsResponse>>

    suspend fun deleteRatingScoreAll(): Resource<BaseResponse<String>>

    suspend fun syncDataRatingScore(): Resource<BaseResponse<String>>
}