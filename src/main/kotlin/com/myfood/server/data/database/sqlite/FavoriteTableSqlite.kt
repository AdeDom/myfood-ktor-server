package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Table

object FavoriteTableSqlite : Table(name = "favorite") {

    val favoriteId = text(name = "favorite_id")
    val userId = text(name = "user_id")
    val foodId = integer(name = "food_id")
    val isFavorite = integer(name = "is_favorite")
    val isBackup = integer(name = "is_backup")
    val created = text(name = "created")
    val updated = text(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(favoriteId, name = "PK_Favorite_ID")
}