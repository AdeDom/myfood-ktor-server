package com.myfood.server.data.resouce.local.rating_score

import com.myfood.server.data.models.entities.RatingScoreEntity

interface RatingScoreLocalDataSource {

    suspend fun getRatingScoreAll(): List<RatingScoreEntity>

    suspend fun replaceRatingScore(
        ratingScoreId: String,
        userId: String,
        foodId: Int,
        ratingScore: Float,
        isBackup: Int,
        created: String,
        updated: String?,
    ): Int?

    suspend fun deleteRatingScoreAll(): Int

    suspend fun findRatingScoreEntityByUserIdAndFoodId(userId: String, foodId: Int): RatingScoreEntity?

    suspend fun getRatingScoreListByBackupIsLocal(): List<RatingScoreEntity>

    suspend fun updateRatingScoreByBackupIsRemote(): Int

    suspend fun replaceRatingScoreAll(ratingScoreList: List<RatingScoreEntity>): Int

    suspend fun getRatingScoreListByFoodId(foodId: Int): List<Float>
}