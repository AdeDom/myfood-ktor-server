package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object FavoriteTable : Table(name = "favorite") {

    val favoriteId = varchar(name = "favorite_id", length = 50)
    val userId = varchar(name = "user_id", length = 50).references(UserTable.userId)
    val foodId = integer(name = "food_id").references(FoodTable.foodId)
    val isFavorite = bool(name = "is_favorite")
    val created = datetime(name = "created")
    val updated = datetime(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(favoriteId, name = "PK_Favorite_ID")
}