package com.myfood.server.data.repositories.profile

import com.myfood.server.data.models.entities.AuthMasterEntity
import com.myfood.server.data.models.entities.UserEntity
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.models.response.UserProfileResponse
import com.myfood.server.data.resouce.local.auth.AuthLocalDataSource
import com.myfood.server.data.resouce.local.user.UserLocalDataSource
import com.myfood.server.data.resouce.remote.profile.ProfileRemoteDataSource
import com.myfood.server.data.resouce.remote.user.UserRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.jwt.JwtHelper

internal class ProfileRepositoryImpl(
    private val jwtHelper: JwtHelper,
    private val userLocalDataSource: UserLocalDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override suspend fun userProfile(userId: String): UserProfileResponse {
        val userLocalAll = userLocalDataSource.getUserAll()
        if (userLocalAll.isEmpty()) {
            val userRemoteAll = userRemoteDataSource.getUserAll()
            userLocalDataSource.deleteUserAll()
            val handleImageProfileDefaultList = handleImageProfileDefaultList(userRemoteAll)
            val listLocalList = userLocalDataSource.insertUserAll(handleImageProfileDefaultList)
            if (listLocalList != handleImageProfileDefaultList.size) {
                userLocalDataSource.deleteUserAll()
            }
        }

        val userEntity = userLocalDataSource.getUserByUserId(userId)
        return if (userEntity != null) {
            UserProfileResponse(
                userId = userEntity.userId,
                email = userEntity.email,
                name = userEntity.name,
                mobileNo = userEntity.mobileNo,
                address = userEntity.address,
                image = userEntity.image,
                status = userEntity.status,
                created = userEntity.created.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
                updated = userEntity.updated?.toString(AppConstant.DATE_TIME_FORMAT_RESPONSE),
            )
        } else {
            throw ApplicationException("User profile is null or empty.")
        }
    }

    private fun handleImageProfileDefaultList(userList: List<UserEntity>): List<UserEntity> {
        return userList.map { userEntity ->
            UserEntity(
                userId = userEntity.userId,
                email = userEntity.email,
                password = userEntity.password,
                name = userEntity.name,
                mobileNo = userEntity.mobileNo,
                address = userEntity.address,
                image = userEntity.image ?: "https://picsum.photos/300/300",
                status = userEntity.status,
                created = userEntity.created,
                updated = userEntity.updated,
            )
        }
    }

    override suspend fun changeProfile(userId: String, changeProfileRequest: ChangeProfileRequest): String {
        val isUpdateUserProfile = profileRemoteDataSource.updateUserProfile(userId, changeProfileRequest) == 1
        return if (isUpdateUserProfile) {
            "Change profile success."
        } else {
            throw ApplicationException("Change profile failed.")
        }
    }

    override suspend fun deleteAccount(userId: String): String {
        val isUpdateStatus = profileRemoteDataSource.updateUserStatus(userId, AppConstant.IN_ACTIVE) == 1
        return if (isUpdateStatus) {
            val getAuthListOriginal = authLocalDataSource.getAuthListByStatusLoginOrRefresh()
            val getAuthList = getAuthListOriginal.map { authEntity ->
                AuthMasterEntity(
                    authId = authEntity.authId,
                    userId = jwtHelper.decodeJwtGetUserId(authEntity.accessToken),
                    status = authEntity.status,
                    isBackup = authEntity.isBackup,
                    created = authEntity.created,
                    updated = authEntity.updated,
                )
            }
            val getAuthIdList = getAuthList
                .filter { it.userId == userId }
                .map { it.authId }
            var updateAuthLogoutCount = 0
            getAuthIdList.forEach { authId ->
                updateAuthLogoutCount += authLocalDataSource.updateAuthStatusLogoutByAuthId(authId)
            }
            if (updateAuthLogoutCount == getAuthIdList.size) {
                "Delete account successfully."
            } else {
                throw ApplicationException("Delete account failed.")
            }
        } else {
            throw ApplicationException("Delete account failed.")
        }
    }
}