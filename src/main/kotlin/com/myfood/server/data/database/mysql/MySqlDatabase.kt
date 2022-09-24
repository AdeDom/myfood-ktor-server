package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Database

internal interface MySqlDatabase {

    fun getDatabase(): Database
}