package com.myfood.server.data.models.entities

data class AuthEntity(
    val authId: String,
    val accessToken: String,
    val refreshToken: String,
    val status: String,
    val isBackup: Int,
    val created: String,
    val updated: String?,
)