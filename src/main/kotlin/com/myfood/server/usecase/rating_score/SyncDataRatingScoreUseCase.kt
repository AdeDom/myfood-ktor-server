package com.myfood.server.usecase.rating_score

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository

internal class SyncDataRatingScoreUseCase(
    private val ratingScoreRepository: RatingScoreRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<String>> {
        return when {
            else -> {
                ratingScoreRepository.syncDataRatingScore()
            }
        }
    }
}