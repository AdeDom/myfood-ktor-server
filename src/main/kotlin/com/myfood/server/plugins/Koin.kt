package com.myfood.server.plugins

import com.myfood.server.di.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal data class DatabaseConfiguration(
    val username: String,
    val password: String,
    val jdbcUrl: String,
)

internal data class JwtConfiguration(
    val secret: String,
    val issuer: String,
    val audience: String,
)

internal fun Application.configureKoin(
    databaseConfiguration: DatabaseConfiguration,
    jwtConfiguration: JwtConfiguration,
) {
    install(Koin) {
        slf4jLogger()
        modules(
            configurationModule(databaseConfiguration, jwtConfiguration),
            localDataSourceModule,
            remoteDataSourceModule,
            repositoryModule,
            domainModule,
        )
    }
}