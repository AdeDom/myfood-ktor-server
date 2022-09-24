package com.myfood.server.usecase.rating_score

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository

internal class GetRatingScoreAllUseCase(
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<List<RatingScoreResponse>>> {
        return when {
            else -> {
                ratingScoreRepository.getRatingScoreAll()
            }
        }
    }
}