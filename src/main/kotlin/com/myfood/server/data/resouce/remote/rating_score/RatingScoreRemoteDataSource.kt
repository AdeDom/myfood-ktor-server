package com.myfood.server.data.resouce.remote.rating_score

import com.myfood.server.data.models.entities.RatingScoreEntity

interface RatingScoreRemoteDataSource {

    suspend fun replaceRatingScoreAll(ratingScoreList: List<RatingScoreEntity>): Int

    suspend fun getRatingScoreAll(): List<RatingScoreEntity>
}