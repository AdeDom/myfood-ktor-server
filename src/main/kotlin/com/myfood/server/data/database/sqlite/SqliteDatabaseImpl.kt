package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

internal class SqliteDatabaseImpl : SqliteDatabase {

    private val database: Database = Database.connect("jdbc:sqlite:./data/my_food.db", "org.sqlite.JDBC")

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun getDatabase(): Database {
        return database
    }
}