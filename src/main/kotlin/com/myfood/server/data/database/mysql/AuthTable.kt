package com.myfood.server.data.database.mysql

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

internal object AuthTable : Table(name = "auth") {

    val authId = varchar(name = "auth_id", length = 50)
    val accessToken = varchar(name = "access_token", length = 500)
    val refreshToken = varchar(name = "refresh_token", length = 500)
    val status = varchar(name = "status", length = 10)
    val created = datetime(name = "created")
    val updated = datetime(name = "updated").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(authId, name = "PK_Auth_ID")
}