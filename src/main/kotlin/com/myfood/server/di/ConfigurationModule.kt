package com.myfood.server.di

import com.myfood.server.data.database.mysql.MySqlDatabase
import com.myfood.server.data.database.mysql.MySqlDatabaseImpl
import com.myfood.server.data.database.sqlite.SqliteDatabase
import com.myfood.server.data.database.sqlite.SqliteDatabaseImpl
import com.myfood.server.plugins.DatabaseConfiguration
import com.myfood.server.plugins.JwtConfiguration
import com.myfood.server.utility.jwt.JwtHelper
import org.koin.dsl.module

internal fun configurationModule(
    databaseConfiguration: DatabaseConfiguration,
    jwtConfiguration: JwtConfiguration,
) = module {

    single<MySqlDatabase> {
        MySqlDatabaseImpl(
            usernameSecret = databaseConfiguration.username,
            passwordSecret = databaseConfiguration.password,
            jdbcUrlSecret = databaseConfiguration.jdbcUrl,
        )
    }
    single<SqliteDatabase> { SqliteDatabaseImpl() }

    single {
        JwtHelper(
            secret = jwtConfiguration.secret,
            issuer = jwtConfiguration.issuer,
            audience = jwtConfiguration.audience,
        )
    }
}