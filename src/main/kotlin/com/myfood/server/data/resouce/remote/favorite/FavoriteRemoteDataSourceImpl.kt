package com.myfood.server.data.resouce.remote.favorite

import com.myfood.server.data.database.mysql.FavoriteTable
import com.myfood.server.data.models.entities.FavoriteEntity
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchReplace
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class FavoriteRemoteDataSourceImpl(
    private val db: Database
) : FavoriteRemoteDataSource {

    override suspend fun replaceFavoriteAll(favoriteList: List<FavoriteEntity>): Int {
        val dateTimeFormat = DateTimeFormat.forPattern(AppConstant.DATE_TIME_FORMAT_REQUEST)
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            FavoriteTable.batchReplace(favoriteList) { favoriteEntity ->
                this[FavoriteTable.favoriteId] = favoriteEntity.favoriteId
                this[FavoriteTable.userId] = favoriteEntity.userId
                this[FavoriteTable.foodId] = favoriteEntity.foodId
                this[FavoriteTable.isFavorite] = favoriteEntity.isFavorite == AppConstant.FAVORITE
                this[FavoriteTable.created] = DateTime.parse(favoriteEntity.created, dateTimeFormat)
                favoriteEntity.updated?.let {
                    this[FavoriteTable.updated] = DateTime.parse(favoriteEntity.updated, dateTimeFormat)
                }
            }
        }

        return statement.size
    }

    override suspend fun getFavoriteAll(): List<FavoriteEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            FavoriteTable
                .slice(
                    FavoriteTable.favoriteId,
                    FavoriteTable.userId,
                    FavoriteTable.foodId,
                    FavoriteTable.isFavorite,
                    FavoriteTable.created,
                    FavoriteTable.updated,
                )
                .selectAll()
                .map { row ->
                    FavoriteEntity(
                        favoriteId = row[FavoriteTable.favoriteId],
                        userId = row[FavoriteTable.userId],
                        foodId = row[FavoriteTable.foodId],
                        isFavorite = if (row[FavoriteTable.isFavorite]) 1 else 0,
                        isBackup = AppConstant.REMOTE_BACKUP,
                        created = row[FavoriteTable.created].toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                        updated = row[FavoriteTable.updated]?.toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                    )
                }
        }
    }
}