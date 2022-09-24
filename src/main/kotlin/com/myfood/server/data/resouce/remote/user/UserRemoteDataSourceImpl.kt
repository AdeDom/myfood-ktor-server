package com.myfood.server.data.resouce.remote.user

import com.myfood.server.data.database.mysql.UserTable
import com.myfood.server.data.models.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal class UserRemoteDataSourceImpl(
    private val db: Database,
) : UserRemoteDataSource {

    override suspend fun getUserAll(): List<UserEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .slice(
                    UserTable.userId,
                    UserTable.email,
                    UserTable.name,
                    UserTable.mobileNo,
                    UserTable.address,
                    UserTable.image,
                    UserTable.status,
                    UserTable.created,
                    UserTable.updated,
                )
                .selectAll()
                .map { row ->
                    UserEntity(
                        userId = row[UserTable.userId],
                        email = row[UserTable.email],
                        password = "",
                        name = row[UserTable.name],
                        mobileNo = row[UserTable.mobileNo],
                        address = row[UserTable.address],
                        image = row[UserTable.image],
                        status = row[UserTable.status],
                        created = row[UserTable.created],
                        updated = row[UserTable.updated],
                    )
                }
        }
    }
}