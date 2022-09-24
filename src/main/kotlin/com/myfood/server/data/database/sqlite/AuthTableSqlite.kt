package com.myfood.server.data.database.sqlite

import org.jetbrains.exposed.sql.Table

object AuthTableSqlite : Table(name = "auth") {

    val authId = text(name = "auth_id")
    val accessToken = text(name = "access_token")
    val refreshToken = text(name = "refresh_token")
    val status = text(name = "status")
    val isBackup = integer(name = "is_backup")
    val created = text(name = "created")
    val updated = text(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(authId, name = "PK_Auth_ID")
}