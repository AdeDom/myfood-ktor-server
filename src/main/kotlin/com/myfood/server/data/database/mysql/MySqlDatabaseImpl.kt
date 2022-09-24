package com.myfood.server.data.database.mysql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class MySqlDatabaseImpl(
    private val mySqlDatabaseConfig: MySqlDatabaseConfig,
) : MySqlDatabase {

    private var database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = mySqlDatabaseConfig.jdbcUrl
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = mySqlDatabaseConfig.username
            password = mySqlDatabaseConfig.password
            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)
        database = Database.connect(dataSource)
    }

    override fun getDatabase(): Database {
        return database
    }
}