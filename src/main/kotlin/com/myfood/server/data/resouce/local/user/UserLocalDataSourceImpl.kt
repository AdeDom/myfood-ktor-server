package com.myfood.server.data.resouce.local.user

import com.myfood.server.data.models.entities.UserEntity

internal class UserLocalDataSourceImpl : UserLocalDataSource {

    private val userList = mutableListOf<UserEntity>()

    override suspend fun getUserByUserId(userId: String): UserEntity? {
        return this.userList
            .map { userEntity ->
                UserEntity(
                    userId = userEntity.userId,
                    email = userEntity.email,
                    password = "",
                    name = userEntity.name,
                    mobileNo = userEntity.mobileNo,
                    address = userEntity.address,
                    image = userEntity.image,
                    status = userEntity.status,
                    created = userEntity.created,
                    updated = userEntity.updated,
                )
            }
            .find { userEntity ->
                userEntity.userId == userId
            }
    }

    override suspend fun getUserAll(): List<UserEntity> {
        return this.userList.map { userEntity ->
            UserEntity(
                userId = userEntity.userId,
                email = userEntity.email,
                password = "",
                name = userEntity.name,
                mobileNo = userEntity.mobileNo,
                address = userEntity.address,
                image = userEntity.image,
                status = userEntity.status,
                created = userEntity.created,
                updated = userEntity.updated,
            )
        }
    }

    override suspend fun insertUserAll(userList: List<UserEntity>): Int {
        this.userList.addAll(userList)
        return this.userList.size
    }

    override suspend fun deleteUserAll() {
        this.userList.clear()
    }
}