package com.myfood.server.plugins

import com.myfood.server.data.database.mysql.MySqlDatabase
import com.myfood.server.data.database.mysql.MySqlDatabaseImpl
import com.myfood.server.data.database.sqlite.SqliteDatabase
import com.myfood.server.data.database.sqlite.SqliteDatabaseImpl
import com.myfood.server.di.domainModule
import com.myfood.server.di.localDataSourceModule
import com.myfood.server.di.remoteDataSourceModule
import com.myfood.server.di.repositoryModule
import com.myfood.server.utility.jwt.JwtHelper
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun Application.configureKoin() {
    val flavor = environment.config.property("database.flavor").getString()
    val databaseNameEnv = environment.config.property("database.$flavor.database_name").getString()
    val usernameEnv = environment.config.property("database.$flavor.username").getString()
    val passwordEnv = environment.config.property("database.$flavor.password").getString()
    val jdbcUrlEnv = environment.config.property("database.$flavor.jdbc_url").getString()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    // koin dependencies injection
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single<MySqlDatabase> {
                    MySqlDatabaseImpl(
                        usernameSecret = usernameEnv,
                        passwordSecret = passwordEnv,
                        jdbcUrlSecret = jdbcUrlEnv,
                    )
                }
                single<SqliteDatabase> { SqliteDatabaseImpl() }

                single {
                    JwtHelper(
                        secret = secret,
                        issuer = issuer,
                        audience = audience,
                    )
                }
            },
            localDataSourceModule,
            remoteDataSourceModule,
            repositoryModule,
            domainModule,
        )
    }
}