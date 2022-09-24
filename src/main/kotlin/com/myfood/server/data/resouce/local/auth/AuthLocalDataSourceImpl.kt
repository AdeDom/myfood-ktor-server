package com.myfood.server.data.resouce.local.auth

import com.myfood.server.data.database.sqlite.AuthTableSqlite
import com.myfood.server.data.models.entities.AuthEntity
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime

class AuthLocalDataSourceImpl(
    private val db: Database,
) : AuthLocalDataSource {

    override suspend fun insertAuth(
        authId: String,
        accessToken: String,
        refreshToken: String,
        status: String,
        isBackup: Int,
    ): Int? {
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite.insert {
                it[AuthTableSqlite.authId] = authId
                it[AuthTableSqlite.accessToken] = accessToken
                it[AuthTableSqlite.refreshToken] = refreshToken
                it[AuthTableSqlite.status] = status
                it[AuthTableSqlite.isBackup] = isBackup
                val currentDateTime = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
                val currentDateTimeString = currentDateTime.toString(AppConstant.DATE_TIME_FORMAT_REQUEST)
                it[created] = currentDateTimeString
                it[updated] = null
            }
        }

        return statement.resultedValues?.size
    }

    override suspend fun getAuthListByStatusLoginOrRefresh(): List<AuthEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite
                .slice(
                    AuthTableSqlite.authId,
                    AuthTableSqlite.accessToken,
                    AuthTableSqlite.refreshToken,
                    AuthTableSqlite.status,
                    AuthTableSqlite.isBackup,
                    AuthTableSqlite.created,
                    AuthTableSqlite.updated,
                )
                .select {
                    (AuthTableSqlite.status eq AppConstant.AUTH_LOGIN) or (AuthTableSqlite.status eq AppConstant.AUTH_REFRESH)
                }
                .map { row ->
                    AuthEntity(
                        authId = row[AuthTableSqlite.authId],
                        accessToken = row[AuthTableSqlite.accessToken],
                        refreshToken = row[AuthTableSqlite.refreshToken],
                        status = row[AuthTableSqlite.status],
                        isBackup = row[AuthTableSqlite.isBackup],
                        created = row[AuthTableSqlite.created],
                        updated = row[AuthTableSqlite.updated],
                    )
                }
        }
    }

    override suspend fun updateAuthStatusLogoutByAuthId(authId: String): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite.update({ AuthTableSqlite.authId eq authId }) {
                it[status] = AppConstant.AUTH_LOGOUT
                val currentDateTime = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
                val currentDateTimeString = currentDateTime.toString(AppConstant.DATE_TIME_FORMAT_REQUEST)
                it[updated] = currentDateTimeString
            }
        }
    }

    override suspend fun updateStatusLogoutByAccessTokenAndRefreshToken(
        accessToken: String,
        refreshToken: String,
    ): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite.update({ (AuthTableSqlite.accessToken eq accessToken) and (AuthTableSqlite.refreshToken eq refreshToken) }) {
                it[status] = AppConstant.AUTH_LOGOUT
                val currentDateTime = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
                val currentDateTimeString = currentDateTime.toString(AppConstant.DATE_TIME_FORMAT_REQUEST)
                it[updated] = currentDateTimeString
            }
        }
    }

    override suspend fun findTokenByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite
                .select {
                    (AuthTableSqlite.accessToken eq accessToken) and (AuthTableSqlite.refreshToken eq refreshToken)
                }
                .count()
        }
    }

    override suspend fun findTokenLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite
                .select {
                    (AuthTableSqlite.accessToken eq accessToken) and (AuthTableSqlite.refreshToken eq refreshToken) and (AuthTableSqlite.status eq AppConstant.AUTH_LOGOUT)
                }
                .count()
        }
    }

    override suspend fun findStatusLoginOrRefreshByAccessToken(accessToken: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite
                .select {
                    AuthTableSqlite.accessToken eq accessToken and ((AuthTableSqlite.status eq AppConstant.AUTH_LOGIN) or (AuthTableSqlite.status eq AppConstant.AUTH_REFRESH))
                }
                .count()
        }
    }

    override suspend fun getAuthListByBackupIsLocal(): List<AuthEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite
                .slice(
                    AuthTableSqlite.authId,
                    AuthTableSqlite.accessToken,
                    AuthTableSqlite.refreshToken,
                    AuthTableSqlite.status,
                    AuthTableSqlite.isBackup,
                    AuthTableSqlite.created,
                    AuthTableSqlite.updated,
                )
                .select {
                    AuthTableSqlite.isBackup eq AppConstant.LOCAL_BACKUP
                }
                .map { row ->
                    AuthEntity(
                        authId = row[AuthTableSqlite.authId],
                        accessToken = row[AuthTableSqlite.accessToken],
                        refreshToken = row[AuthTableSqlite.refreshToken],
                        status = row[AuthTableSqlite.status],
                        isBackup = row[AuthTableSqlite.isBackup],
                        created = row[AuthTableSqlite.created],
                        updated = row[AuthTableSqlite.updated],
                    )
                }
        }
    }

    override suspend fun updateAuthByBackupIsRemote(): Int {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite.update({ AuthTableSqlite.isBackup eq AppConstant.LOCAL_BACKUP }) {
                it[isBackup] = AppConstant.REMOTE_BACKUP
            }
        }
    }

    override suspend fun replaceAuthAll(authList: List<AuthEntity>): Int {
        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            AuthTableSqlite.batchReplace(authList) { authEntity ->
                this[AuthTableSqlite.authId] = authEntity.authId
                this[AuthTableSqlite.accessToken] = authEntity.accessToken
                this[AuthTableSqlite.refreshToken] = authEntity.refreshToken
                this[AuthTableSqlite.status] = authEntity.status
                this[AuthTableSqlite.isBackup] = authEntity.isBackup
                this[AuthTableSqlite.created] = authEntity.created
                this[AuthTableSqlite.updated] = authEntity.updated
            }
        }

        return statement.size
    }
}