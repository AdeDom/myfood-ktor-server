package com.myfood.server.data.repositories.rating_score

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.response.RatingScoreResponse
import com.myfood.server.data.models.web_sockets.RatingScoreWebSocketsResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.resouce.local.rating_score.RatingScoreLocalDataSource
import com.myfood.server.data.resouce.remote.rating_score.RatingScoreRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import org.joda.time.DateTime
import java.text.DecimalFormat
import java.util.*

class RatingScoreRepositoryImpl(
    private val ratingScoreLocalDataSource: RatingScoreLocalDataSource,
    private val ratingScoreRemoteDataSource: RatingScoreRemoteDataSource,
) : RatingScoreRepository {

    override suspend fun getRatingScoreAll(): Resource<BaseResponse<List<RatingScoreResponse>>> {
        val response = BaseResponse<List<RatingScoreResponse>>()

        val ratingScoreEntityList = ratingScoreLocalDataSource.getRatingScoreAll()
        val ratingScoreResponseList = ratingScoreEntityList.map { ratingScoreEntity ->
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
        response.result = ratingScoreResponseList
        response.status = ResponseKeyConstant.SUCCESS
        return Resource.Success(response)
    }

    override suspend fun myRatingScore(
        userId: String,
        foodId: Int,
        ratingScore: Float,
    ): Resource<BaseResponse<RatingScoreWebSocketsResponse>> {
        val response = BaseResponse<RatingScoreWebSocketsResponse>()

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
            val ratingScoreWebSocketsResponse = RatingScoreWebSocketsResponse(
                foodId = foodId,
                ratingScore = ratingScoreResponse,
                ratingScoreCount = ratingScoreCountResponse,
            )
            response.result = ratingScoreWebSocketsResponse
            response.status = ResponseKeyConstant.SUCCESS
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "Rating score is failed.")
            Resource.Error(response)
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

    override suspend fun deleteRatingScoreAll(): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val ratingScoreEntityList = ratingScoreLocalDataSource.getRatingScoreAll()
        val deleteRatingScoreCount = ratingScoreLocalDataSource.deleteRatingScoreAll()
        return if (deleteRatingScoreCount == ratingScoreEntityList.size) {
            response.result = "Delete rating score all is success."
            response.status = ResponseKeyConstant.SUCCESS
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "Delete rating score all is failed.")
            Resource.Error(response)
        }
    }

    override suspend fun syncDataRatingScore(): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val ratingScoreLocalList = ratingScoreLocalDataSource.getRatingScoreListByBackupIsLocal()
        val replaceRatingScoreRemoteCount = ratingScoreRemoteDataSource.replaceRatingScoreAll(ratingScoreLocalList)
        return if (ratingScoreLocalList.size == replaceRatingScoreRemoteCount) {
            val updateRatingScoreBackupCount = ratingScoreLocalDataSource.updateRatingScoreByBackupIsRemote()
            if (ratingScoreLocalList.size == updateRatingScoreBackupCount) {
                val ratingScoreRemoteList = ratingScoreRemoteDataSource.getRatingScoreAll()
                val replaceRatingScoreLocalCount =
                    ratingScoreLocalDataSource.replaceRatingScoreAll(ratingScoreRemoteList)
                if (ratingScoreRemoteList.size == replaceRatingScoreLocalCount) {
                    response.result = "Sync data success."
                    response.status = ResponseKeyConstant.SUCCESS
                    Resource.Success(response)
                } else {
                    response.error = BaseError(message = "Sync data failed (3).")
                    Resource.Error(response)
                }
            } else {
                response.error = BaseError(message = "Sync data failed (2).")
                Resource.Error(response)
            }
        } else {
            response.error = BaseError(message = "Sync data failed (1).")
            Resource.Error(response)
        }
    }
}