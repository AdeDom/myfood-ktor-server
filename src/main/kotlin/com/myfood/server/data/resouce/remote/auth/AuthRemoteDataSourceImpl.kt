package com.myfood.server.data.resouce.remote.auth

import com.myfood.server.data.database.mysql.AuthTable
import com.myfood.server.data.database.mysql.UserTable
import com.myfood.server.data.models.entities.AuthEntity
import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AuthRemoteDataSourceImpl(
    private val db: Database,
) : AuthRemoteDataSource {

    override suspend fun findUserIdByEmailAndPassword(email: String, password: String, status: String): String? {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .slice(
                    UserTable.userId,
                )
                .select {
                    (UserTable.email eq email) and (UserTable.password eq password) and (UserTable.status eq status)
                }
                .map { row ->
                    row[UserTable.userId]
                }
                .singleOrNull()
        }
    }

    override suspend fun findUserByEmail(email: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .slice(
                    UserTable.email,
                )
                .select {
                    UserTable.email eq email
                }
                .count()
        }
    }

    override suspend fun insertUser(userId: String, registerRequest: RegisterRequest, status: String): Int? {
        val (email, password, name, mobileNo, address) = registerRequest

        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .insert {
                    it[UserTable.userId] = userId
                    it[UserTable.email] = email!!
                    it[UserTable.password] = password!!
                    it[UserTable.name] = name!!
                    it[UserTable.mobileNo] = mobileNo
                    it[UserTable.address] = address
                    it[UserTable.status] = status
                    it[created] = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
                    it[updated] = null
                }
        }

        return statement.resultedValues?.size
    }

    override suspend fun findUserByUserIdAndPassword(userId: String, password: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable
                .select {
                    (UserTable.userId eq userId) and (UserTable.password eq password)
                }
                .count()
        }
    }

    override suspend fun updateUserPassword(userId: String, password: String): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            UserTable.update({ UserTable.userId eq userId }) {
                it[UserTable.password] = password
                it[updated] = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
            }
        }
    }

    override suspend fun replaceAuthAll(authList: List<AuthEntity>): Int {
        val dateTimeFormat = DateTimeFormat.forPattern(AppConstant.DATE_TIME_FORMAT_REQUEST)
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTable.batchReplace(authList) { authEntity ->
                this[AuthTable.authId] = authEntity.authId
                this[AuthTable.accessToken] = authEntity.accessToken
                this[AuthTable.refreshToken] = authEntity.refreshToken
                this[AuthTable.status] = authEntity.status
                this[AuthTable.created] = DateTime.parse(authEntity.created, dateTimeFormat)
                authEntity.updated?.let {
                    this[AuthTable.updated] = DateTime.parse(authEntity.updated, dateTimeFormat)
                }
            }
        }

        return statement.size
    }

    override suspend fun getAuthAll(): List<AuthEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTable
                .slice(
                    AuthTable.authId,
                    AuthTable.accessToken,
                    AuthTable.refreshToken,
                    AuthTable.status,
                    AuthTable.created,
                    AuthTable.updated,
                )
                .selectAll()
                .map { row ->
                    AuthEntity(
                        authId = row[AuthTable.authId],
                        accessToken = row[AuthTable.accessToken],
                        refreshToken = row[AuthTable.refreshToken],
                        status = row[AuthTable.status],
                        isBackup = AppConstant.REMOTE_BACKUP,
                        created = row[AuthTable.created].toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                        updated = row[AuthTable.updated]?.toString(AppConstant.DATE_TIME_FORMAT_REQUEST),
                    )
                }
        }
    }
}