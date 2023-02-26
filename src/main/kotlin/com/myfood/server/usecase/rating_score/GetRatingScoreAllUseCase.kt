package com.myfood.server.usecase.rating_score

import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository

internal class GetRatingScoreAllUseCase(
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(): List<RatingScoreResponse> {
        return ratingScoreRepository.getRatingScoreAll()
    }
}