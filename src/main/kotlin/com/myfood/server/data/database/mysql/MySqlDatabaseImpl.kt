package com.myfood.server.data.database.mysql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class MySqlDatabaseImpl(
    private val usernameSecret: String,
    private val passwordSecret: String,
    private val jdbcUrlSecret: String,
) : MySqlDatabase {

    private var database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = jdbcUrlSecret
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = usernameSecret
            password = passwordSecret
            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)
        database = Database.connect(dataSource)
    }

    override fun getDatabase(): Database {
        return database
    }
}