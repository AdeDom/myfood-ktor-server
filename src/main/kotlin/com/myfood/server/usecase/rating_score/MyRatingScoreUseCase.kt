package com.myfood.server.usecase.rating_score

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository
import com.myfood.server.usecase.auth.TokenUseCase

internal class MyRatingScoreUseCase(
    private val tokenUseCase: TokenUseCase,
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(
        authKey: String?,
        myRatingScoreRequest: MyRatingScoreRequest,
    ): Resource<BaseResponse<RatingScoreWebSocketsResponse>> {
        val response = BaseResponse<RatingScoreWebSocketsResponse>()

        val (foodId, ratingScore) = myRatingScoreRequest
        return when {
            tokenUseCase.isValidateToken(authKey) -> {
                response.error = tokenUseCase.getBaseError(authKey)
                Resource.Error(response)
            }

            foodId == null -> {
                response.error = BaseError(message = "Food id is null.")
                Resource.Error(response)
            }

            ratingScore == null -> {
                response.error = BaseError(message = "Rating score is null.")
                Resource.Error(response)
            }

            ratingScore !in 0F..5F -> {
                response.error = BaseError(message = "Rating score invalid.")
                Resource.Error(response)
            }

            else -> {
                val userId = tokenUseCase.getUserId(authKey)
                ratingScoreRepository.myRatingScore(userId, foodId, ratingScore)
            }
        }
    }
}