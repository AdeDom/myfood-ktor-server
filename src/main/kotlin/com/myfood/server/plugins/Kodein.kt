package com.myfood.server.plugins

import com.myfood.server.data.database.mysql.MySqlDatabase
import com.myfood.server.data.database.mysql.MySqlDatabaseConfig
import com.myfood.server.data.database.mysql.MySqlDatabaseImpl
import com.myfood.server.data.database.sqlite.SqliteDatabase
import com.myfood.server.data.database.sqlite.SqliteDatabaseImpl
import com.myfood.server.di.domainModule
import com.myfood.server.di.localDataSourceModule
import com.myfood.server.di.remoteDataSourceModule
import com.myfood.server.di.repositoryModule
import com.myfood.server.utility.jwt.JwtConfig
import com.myfood.server.utility.jwt.JwtHelper
import io.ktor.server.application.*
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Application.configureKodein() {
    val databaseNameEnv = environment.config.property("my_food_db.database_name").getString()
    val usernameEnv = environment.config.property("my_food_db.username").getString()
    val passwordEnv = environment.config.property("my_food_db.password").getString()
    val jdbcUrlEnv = environment.config.property("my_food_db.jdbc_url").getString()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    // kodein dependencies injection
    di {
        // database
        bindSingleton {
            MySqlDatabaseConfig(
                databaseName = databaseNameEnv,
                username = usernameEnv,
                password = passwordEnv,
                jdbcUrl = jdbcUrlEnv,
            )
        }
        bindSingleton<MySqlDatabase> { MySqlDatabaseImpl(instance()) }
        bindSingleton<SqliteDatabase> { SqliteDatabaseImpl() }

        // structure
        importAll(
            localDataSourceModule,
            remoteDataSourceModule,
            repositoryModule,
            domainModule,
        )

        // jwt
        bindSingleton {
            JwtConfig(
                secret = secret,
                issuer = issuer,
                audience = audience,
                realm = myRealm,
            )
        }
        bindSingleton { JwtHelper(instance()) }
    }
}