package com.myfood.server.data.repositories.rating_score

import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.resouce.local.rating_score.RatingScoreLocalDataSource
import com.myfood.server.data.resouce.remote.rating_score.RatingScoreRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.exception.ApplicationException
import org.joda.time.DateTime
import java.text.DecimalFormat
import java.util.*

internal class RatingScoreRepositoryImpl(
    private val ratingScoreLocalDataSource: RatingScoreLocalDataSource,
    private val ratingScoreRemoteDataSource: RatingScoreRemoteDataSource,
) : RatingScoreRepository {

    override suspend fun getRatingScoreAll(): List<RatingScoreResponse> {
        return ratingScoreLocalDataSource.getRatingScoreAll().map { ratingScoreEntity ->
            RatingScoreResponse(
                ratingScoreId = ratingScoreEntity.ratingScoreId,
                userId = ratingScoreEntity.userId,
                foodId = ratingScoreEntity.foodId,
                ratingScore = ratingScoreEntity.ratingScore,
                isBackup = ratingScoreEntity.isBackup == AppConstant.REMOTE_BACKUP,
                created = ratingScoreEntity.created,
                updated = ratingScoreEntity.updated,
            )
        }
    }

    override suspend fun myRatingScore(
        userId: String,
        foodId: Int,
        ratingScore: Float,
    ): RatingScoreWebSocketsResponse {
        val ratingScoreEntity = ratingScoreLocalDataSource.findRatingScoreEntityByUserIdAndFoodId(userId, foodId)
        val ratingScoreIdForCreated = UUID.randomUUID().toString().replace("-", "")
        val currentDateTime = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
        val currentDateTimeString = currentDateTime.toString(AppConstant.DATE_TIME_FORMAT_REQUEST)
        val isBackup = AppConstant.LOCAL_BACKUP
        val created = ratingScoreEntity?.created ?: currentDateTimeString
        val updated = if (ratingScoreEntity != null) currentDateTimeString else null
        val myRatingScoreCount = ratingScoreLocalDataSource.replaceRatingScore(
            ratingScoreEntity?.ratingScoreId ?: ratingScoreIdForCreated,
            userId,
            foodId,
            ratingScore,
            isBackup,
            created,
            updated,
        ) ?: 0
        return if (myRatingScoreCount == 1) {
            val ratingScoreAll = ratingScoreLocalDataSource.getRatingScoreListByFoodId(foodId)
            val ratingScoreResponse = if (ratingScoreAll.isNotEmpty()) {
                ratingScoreAll.sum() / ratingScoreAll.size
            } else {
                null
            }
            val ratingScoreCountResponse = toRatingScoreCount(ratingScoreAll.size)
            RatingScoreWebSocketsResponse(
                foodId = foodId,
                ratingScore = ratingScoreResponse,
                ratingScoreCount = ratingScoreCountResponse,
            )
        } else {
            throw ApplicationException("Rating score is failed.")
        }
    }

    private fun toRatingScoreCount(ratingScore: Int?): String? {
        val hasRatingScore = ratingScore != null && ratingScore != 0
        return if (hasRatingScore) {
            val decimalFormat = DecimalFormat("#,###")
            decimalFormat.format(ratingScore)
        } else {
            null
        }
    }

    override suspend fun deleteRatingScoreAll(): String {
        val ratingScoreEntityList = ratingScoreLocalDataSource.getRatingScoreAll()
        val deleteRatingScoreCount = ratingScoreLocalDataSource.deleteRatingScoreAll()
        return if (deleteRatingScoreCount == ratingScoreEntityList.size) {
            "Delete rating score all is success."
        } else {
            throw ApplicationException("Delete rating score all is failed.")
        }
    }

    override suspend fun syncDataRatingScore(): String {
        val ratingScoreLocalList = ratingScoreLocalDataSource.getRatingScoreListByBackupIsLocal()
        val replaceRatingScoreRemoteCount = ratingScoreRemoteDataSource.replaceRatingScoreAll(ratingScoreLocalList)
        return if (ratingScoreLocalList.size == replaceRatingScoreRemoteCount) {
            val updateRatingScoreBackupCount = ratingScoreLocalDataSource.updateRatingScoreByBackupIsRemote()
            if (ratingScoreLocalList.size == updateRatingScoreBackupCount) {
                val ratingScoreRemoteList = ratingScoreRemoteDataSource.getRatingScoreAll()
                val replaceRatingScoreLocalCount =
                    ratingScoreLocalDataSource.replaceRatingScoreAll(ratingScoreRemoteList)
                if (ratingScoreRemoteList.size == replaceRatingScoreLocalCount) {
                    "Sync data success."
                } else {
                    throw ApplicationException("Sync data failed (3).")
                }
            } else {
                throw ApplicationException("Sync data failed (2).")
            }
        } else {
            throw ApplicationException("Sync data failed (1).")
        }
    }
}