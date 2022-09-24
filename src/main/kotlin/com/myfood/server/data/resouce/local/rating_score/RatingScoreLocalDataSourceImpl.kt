package com.myfood.server.data.resouce.local.rating_score

import com.myfood.server.data.database.sqlite.RatingScoreTableSqlite
import com.myfood.server.data.models.entities.RatingScoreEntity
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RatingScoreLocalDataSourceImpl(
    private val db: Database
) : RatingScoreLocalDataSource {

    override suspend fun getRatingScoreAll(): List<RatingScoreEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite
                .slice(
                    RatingScoreTableSqlite.ratingScoreId,
                    RatingScoreTableSqlite.userId,
                    RatingScoreTableSqlite.foodId,
                    RatingScoreTableSqlite.ratingScore,
                    RatingScoreTableSqlite.isBackup,
                    RatingScoreTableSqlite.created,
                    RatingScoreTableSqlite.updated,
                )
                .selectAll()
                .map { row ->
                    RatingScoreEntity(
                        ratingScoreId = row[RatingScoreTableSqlite.ratingScoreId],
                        userId = row[RatingScoreTableSqlite.userId],
                        foodId = row[RatingScoreTableSqlite.foodId],
                        ratingScore = row[RatingScoreTableSqlite.ratingScore],
                        isBackup = row[RatingScoreTableSqlite.isBackup],
                        created = row[RatingScoreTableSqlite.created],
                        updated = row[RatingScoreTableSqlite.updated],
                    )
                }
        }
    }

    override suspend fun replaceRatingScore(
        ratingScoreId: String,
        userId: String,
        foodId: Int,
        ratingScore: Float,
        isBackup: Int,
        created: String,
        updated: String?
    ): Int? {
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite.replace {
                it[RatingScoreTableSqlite.ratingScoreId] = ratingScoreId
                it[RatingScoreTableSqlite.userId] = userId
                it[RatingScoreTableSqlite.foodId] = foodId
                it[RatingScoreTableSqlite.ratingScore] = ratingScore
                it[RatingScoreTableSqlite.isBackup] = isBackup
                it[RatingScoreTableSqlite.created] = created
                it[RatingScoreTableSqlite.updated] = updated
            }
        }

        return statement.resultedValues?.size
    }

    override suspend fun deleteRatingScoreAll(): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite.deleteAll()
        }
    }

    override suspend fun findRatingScoreEntityByUserIdAndFoodId(userId: String, foodId: Int): RatingScoreEntity? {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite
                .slice(
                    RatingScoreTableSqlite.ratingScoreId,
                    RatingScoreTableSqlite.userId,
                    RatingScoreTableSqlite.foodId,
                    RatingScoreTableSqlite.ratingScore,
                    RatingScoreTableSqlite.isBackup,
                    RatingScoreTableSqlite.created,
                    RatingScoreTableSqlite.updated,
                )
                .select {
                    (RatingScoreTableSqlite.userId eq userId) and (RatingScoreTableSqlite.foodId eq foodId)
                }
                .map { row ->
                    RatingScoreEntity(
                        ratingScoreId = row[RatingScoreTableSqlite.ratingScoreId],
                        userId = row[RatingScoreTableSqlite.userId],
                        foodId = row[RatingScoreTableSqlite.foodId],
                        ratingScore = row[RatingScoreTableSqlite.ratingScore],
                        isBackup = row[RatingScoreTableSqlite.isBackup],
                        created = row[RatingScoreTableSqlite.created],
                        updated = row[RatingScoreTableSqlite.updated],
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun getRatingScoreListByBackupIsLocal(): List<RatingScoreEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite
                .slice(
                    RatingScoreTableSqlite.ratingScoreId,
                    RatingScoreTableSqlite.userId,
                    RatingScoreTableSqlite.foodId,
                    RatingScoreTableSqlite.ratingScore,
                    RatingScoreTableSqlite.isBackup,
                    RatingScoreTableSqlite.created,
                    RatingScoreTableSqlite.updated,
                )
                .select {
                    RatingScoreTableSqlite.isBackup eq AppConstant.LOCAL_BACKUP
                }
                .map { row ->
                    RatingScoreEntity(
                        ratingScoreId = row[RatingScoreTableSqlite.ratingScoreId],
                        userId = row[RatingScoreTableSqlite.userId],
                        foodId = row[RatingScoreTableSqlite.foodId],
                        ratingScore = row[RatingScoreTableSqlite.ratingScore],
                        isBackup = row[RatingScoreTableSqlite.isBackup],
                        created = row[RatingScoreTableSqlite.created],
                        updated = row[RatingScoreTableSqlite.updated],
                    )
                }
        }
    }

    override suspend fun updateRatingScoreByBackupIsRemote(): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite.update({ RatingScoreTableSqlite.isBackup eq AppConstant.LOCAL_BACKUP }) {
                it[isBackup] = AppConstant.REMOTE_BACKUP
            }
        }
    }

    override suspend fun replaceRatingScoreAll(ratingScoreList: List<RatingScoreEntity>): Int {
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite.batchReplace(ratingScoreList) { ratingScoreEntity ->
                this[RatingScoreTableSqlite.ratingScoreId] = ratingScoreEntity.ratingScoreId
                this[RatingScoreTableSqlite.userId] = ratingScoreEntity.userId
                this[RatingScoreTableSqlite.foodId] = ratingScoreEntity.foodId
                this[RatingScoreTableSqlite.ratingScore] = ratingScoreEntity.ratingScore
                this[RatingScoreTableSqlite.isBackup] = ratingScoreEntity.isBackup
                this[RatingScoreTableSqlite.created] = ratingScoreEntity.created
                this[RatingScoreTableSqlite.updated] = ratingScoreEntity.updated
            }
        }

        return statement.size
    }

    override suspend fun getRatingScoreListByFoodId(foodId: Int): List<Float> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            RatingScoreTableSqlite
                .slice(
                    RatingScoreTableSqlite.ratingScore,
                )
                .select {
                    RatingScoreTableSqlite.foodId eq foodId
                }
                .map { row ->
                    row[RatingScoreTableSqlite.ratingScore]
                }
        }
    }
}