package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Database

interface SqliteDatabase {

    fun getDatabase(): Database
}