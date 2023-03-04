package com.myfood.server.data.database.mysql

import com.myfood.server.plugins.DatabaseConfiguration
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlDatabaseImpl(
    private val databaseConfiguration: DatabaseConfiguration,
) : MySqlDatabase {

    private var database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = databaseConfiguration.jdbcUrl
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = databaseConfiguration.username
            password = databaseConfiguration.password
            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)
        database = Database.connect(dataSource)

        transaction {
            SchemaUtils.create(CategoryTable)
            SchemaUtils.create(FavoriteTable)
            SchemaUtils.create(FoodTable)
            SchemaUtils.create(RatingScoreTable)
            SchemaUtils.create(UserTable)
        }
    }

    override fun getDatabase(): Database {
        return database
    }
}