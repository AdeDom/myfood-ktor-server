package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object RatingScoreTable : Table(name = "rating_score") {

    val ratingScoreId = varchar(name = "rating_score_id", length = 50)
    val userId = varchar(name = "user_id", length = 50).references(UserTable.userId)
    val foodId = integer(name = "food_id").references(FoodTable.foodId)
    val ratingScore = float(name = "rating_score")
    val created = datetime(name = "created")
    val updated = datetime(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(ratingScoreId, name = "PK_RatingScore_ID")
}