package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Table

internal object RatingScoreTableSqlite : Table(name = "rating_score") {

    val ratingScoreId = text(name = "rating_score_id")
    val userId = text(name = "user_id")
    val foodId = integer(name = "food_id")
    val ratingScore = float(name = "rating_score")
    val isBackup = integer(name = "is_backup")
    val created = text(name = "created")
    val updated = text(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(ratingScoreId, name = "PK_RatingScore_ID")
}