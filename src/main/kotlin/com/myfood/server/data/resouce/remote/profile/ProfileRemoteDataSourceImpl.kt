package com.myfood.server.data.resouce.remote.profile

import com.myfood.server.data.database.mysql.UserTable
import com.myfood.server.data.models.entities.UserEntity
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class ProfileRemoteDataSourceImpl(
    private val db: Database,
) : ProfileRemoteDataSource {

    override suspend fun getUserByUserId(userId: String): UserEntity? {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .slice(
                    UserTable.userId,
                    UserTable.email,
                    UserTable.password,
                    UserTable.name,
                    UserTable.mobileNo,
                    UserTable.address,
                    UserTable.image,
                    UserTable.status,
                    UserTable.created,
                    UserTable.updated,
                )
                .select {
                    UserTable.userId eq userId
                }
                .map { row ->
                    UserEntity(
                        userId = row[UserTable.userId],
                        email = row[UserTable.email],
                        password = row[UserTable.password],
                        name = row[UserTable.name],
                        mobileNo = row[UserTable.mobileNo],
                        address = row[UserTable.address],
                        image = row[UserTable.image],
                        status = row[UserTable.status],
                        created = row[UserTable.created],
                        updated = row[UserTable.updated],
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun updateUserProfile(userId: String, changeProfileRequest: ChangeProfileRequest): Int {
        val (name, mobileNo, address) = changeProfileRequest

        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable.update({ UserTable.userId eq userId }) {
                it[UserTable.name] = name!!
                it[UserTable.mobileNo] = mobileNo
                it[UserTable.address] = address
                it[updated] = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
            }
        }
    }

    override suspend fun updateUserStatus(userId: String, status: String): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable.update({ UserTable.userId eq userId }) {
                it[UserTable.status] = status
                it[updated] = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
            }
        }
    }
}