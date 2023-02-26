package com.myfood.server.usecase.rating_score

import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository
import com.myfood.server.usecase.auth.TokenUseCase
import com.myfood.server.utility.exception.ApplicationException

internal class MyRatingScoreUseCase(
    private val tokenUseCase: TokenUseCase,
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(
        authKey: String?,
        myRatingScoreRequest: MyRatingScoreRequest,
    ): RatingScoreWebSocketsResponse {
        val (foodId, ratingScore) = myRatingScoreRequest
        return when {
            tokenUseCase.isValidateToken(authKey) -> throw ApplicationException(tokenUseCase.getBaseError(authKey))
            foodId == null -> throw ApplicationException("Food id is null.")
            ratingScore == null -> throw ApplicationException("Rating score is null.")
            ratingScore !in 0F..5F -> throw ApplicationException("Rating score invalid.")
            else -> ratingScoreRepository.myRatingScore(tokenUseCase.getUserId(authKey), foodId, ratingScore)
        }
    }
}