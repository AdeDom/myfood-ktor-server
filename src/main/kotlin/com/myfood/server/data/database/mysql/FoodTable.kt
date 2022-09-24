package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

internal object FoodTable : Table(name = "food") {

    val foodId = integer(name = "food_id").autoIncrement()
    val foodName = varchar(name = "food_name", length = 100)
    val alias = varchar(name = "alias", length = 200).nullable()
    val image = varchar(name = "image", length = 500)
    val price = double(name = "price")
    val description = varchar(name = "description", length = 1000).nullable()
    val categoryId = integer(name = "category_id").references(CategoryTable.categoryId)
    val status = varchar(name = "status", length = 10)
    val created = datetime(name = "created")
    val updated = datetime(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(foodId, name = "PK_Food_ID")
}