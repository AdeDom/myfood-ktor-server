package com.myfood.server.data.database.mysql

data class MySqlDatabaseConfig(
    val databaseName: String,
    val username: String,
    val password: String,
    val jdbcUrl: String,
)