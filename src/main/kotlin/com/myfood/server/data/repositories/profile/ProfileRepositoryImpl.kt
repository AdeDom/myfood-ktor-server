package com.myfood.server.data.repositories.profile

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.entities.AuthMasterEntity
import com.myfood.server.data.models.entities.UserEntity
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.models.response.UserProfileResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.resouce.local.auth.AuthLocalDataSource
import com.myfood.server.data.resouce.local.user.UserLocalDataSource
import com.myfood.server.data.resouce.remote.profile.ProfileRemoteDataSource
import com.myfood.server.data.resouce.remote.user.UserRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.jwt.JwtHelper

class ProfileRepositoryImpl(
    private val jwtHelper: JwtHelper,
    private val userLocalDataSource: UserLocalDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override suspend fun userProfile(userId: String): Resource<BaseResponse<UserProfileResponse>> {
        val response = BaseResponse<UserProfileResponse>()

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
            val userProfileResponse = UserProfileResponse(
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
            response.status = ResponseKeyConstant.SUCCESS
            response.result = userProfileResponse
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "User profile is null or empty.")
            Resource.Error(response)
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

    override suspend fun changeProfile(
        userId: String,
        changeProfileRequest: ChangeProfileRequest,
    ): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val isUpdateUserProfile = profileRemoteDataSource.updateUserProfile(userId, changeProfileRequest) == 1
        return if (isUpdateUserProfile) {
            response.status = ResponseKeyConstant.SUCCESS
            response.result = "Change profile success."
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "Change profile failed.")
            Resource.Error(response)
        }
    }

    override suspend fun deleteAccount(userId: String): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

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
                response.status = ResponseKeyConstant.SUCCESS
                response.result = "Delete account successfully."
                Resource.Success(response)
            } else {
                response.error = BaseError(message = "Delete account failed.")
                Resource.Error(response)
            }
        } else {
            response.error = BaseError(message = "Delete account failed.")
            Resource.Error(response)
        }
    }
}