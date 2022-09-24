package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

internal object CategoryTable : Table(name = "category") {

    val categoryId = integer(name = "category_id").autoIncrement()
    val categoryName = varchar(name = "category_name", length = 100)
    val image = varchar(name = "image", length = 500)
    val categoryTypeName = varchar(name = "category_type_name", length = 100).default("normal")
    val created = datetime(name = "created")
    val updated = datetime(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(categoryId, name = "PK_Category_ID")
}