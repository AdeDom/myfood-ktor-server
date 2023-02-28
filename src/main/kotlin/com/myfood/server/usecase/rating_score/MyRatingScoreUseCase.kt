package com.myfood.server.usecase.rating_score

import com.auth0.jwt.JWT
import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.jwt.JwtHelper

internal class MyRatingScoreUseCase(
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(
        authKey: String?,
        myRatingScoreRequest: MyRatingScoreRequest,
    ): RatingScoreWebSocketsResponse {
        val (foodId, ratingScore) = myRatingScoreRequest
        return when {
            foodId == null -> throw ApplicationException("Food id is null.")
            ratingScore == null -> throw ApplicationException("Rating score is null.")
            ratingScore !in 0F..5F -> throw ApplicationException("Rating score invalid.")
            else -> {
                val accessToken = authKey?.replace("Bearer", "")?.trim()
                val userId = JWT().decodeJwt(accessToken).getClaim(JwtHelper.USER_ID).asString()
                ratingScoreRepository.myRatingScore(userId, foodId, ratingScore)
            }
        }
    }
}