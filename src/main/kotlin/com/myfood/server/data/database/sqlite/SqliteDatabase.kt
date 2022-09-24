package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Database

internal interface SqliteDatabase {

    fun getDatabase(): Database
}