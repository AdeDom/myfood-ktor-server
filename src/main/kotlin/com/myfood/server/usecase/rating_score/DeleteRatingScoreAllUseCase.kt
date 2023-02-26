package com.myfood.server.usecase.rating_score

import com.myfood.server.data.repositories.rating_score.RatingScoreRepository

internal class DeleteRatingScoreAllUseCase(
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(): String {
        return ratingScoreRepository.deleteRatingScoreAll()
    }
}