package com.myfood.server.data.resouce.remote.rating_score

import com.myfood.server.data.database.mysql.RatingScoreTable
import com.myfood.server.data.models.entities.RatingScoreEntity
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchReplace
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class RatingScoreRemoteDataSourceImpl(
    private val db: Database
) : RatingScoreRemoteDataSource {

    override suspend fun replaceRatingScoreAll(ratingScoreList: List<RatingScoreEntity>): Int {
        val dateTimeFormat = DateTimeFormat.forPattern(AppConstant.DATE_TIME_FORMAT_REQUEST)
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTable.batchReplace(ratingScoreList) { ratingScoreEntity ->
                this[RatingScoreTable.ratingScoreId] = ratingScoreEntity.ratingScoreId
                this[RatingScoreTable.userId] = ratingScoreEntity.userId
                this[RatingScoreTable.foodId] = ratingScoreEntity.foodId
                this[RatingScoreTable.ratingScore] = ratingScoreEntity.ratingScore
                this[RatingScoreTable.created] = DateTime.parse(ratingScoreEntity.created, dateTimeFormat)
                ratingScoreEntity.updated?.let {
                    this[RatingScoreTable.updated] = DateTime.parse(ratingScoreEntity.updated, dateTimeFormat)
                }
            }
        }

        return statement.size
    }

    override suspend fun getRatingScoreAll(): List<RatingScoreEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTable
                .slice(
                    RatingScoreTable.ratingScoreId,
                    RatingScoreTable.userId,
                    RatingScoreTable.foodId,
                    RatingScoreTable.ratingScore,
                    RatingScoreTable.created,
                    RatingScoreTable.updated,
                )
                .selectAll()
                .map { row ->
                    RatingScoreEntity(
                        ratingScoreId = row[RatingScoreTable.ratingScoreId],
                        userId = row[RatingScoreTable.userId],
                        foodId = row[RatingScoreTable.foodId],
                        ratingScore = row[RatingScoreTable.ratingScore],
                        isBackup = AppConstant.REMOTE_BACKUP,
                        created = row[RatingScoreTable.created].toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                        updated = row[RatingScoreTable.updated]?.toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                    )
                }
        }
    }
}