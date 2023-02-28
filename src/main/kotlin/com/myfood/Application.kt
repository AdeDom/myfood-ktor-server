package com.myfood

import com.myfood.server.plugins.*
import io.ktor.server.application.*

internal fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

internal fun Application.module() {
    val flavor = environment.config.property("database.flavor").getString()
    val databaseNameEnv = environment.config.property("database.$flavor.database_name").getString()
    val usernameEnv = environment.config.property("database.$flavor.username").getString()
    val passwordEnv = environment.config.property("database.$flavor.password").getString()
    val jdbcUrlEnv = environment.config.property("database.$flavor.jdbc_url").getString()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    configureDefaultHeaders()
    configureCallLogging()
    configureContentNegotiation()
    configureAuthentication(realm)
    configureWebSockets()
    configureKoin(
        databaseConfiguration = DatabaseConfiguration(
            username = usernameEnv,
            password = passwordEnv,
            jdbcUrl = jdbcUrlEnv,
        ),
        jwtConfiguration = JwtConfiguration(
            secret = secret,
            issuer = issuer,
            audience = audience,
        ),
    )
    configureRouting()
}
