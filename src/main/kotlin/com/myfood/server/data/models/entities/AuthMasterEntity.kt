package com.myfood.server.data.models.entities

internal data class AuthMasterEntity(
    val authId: String,
    val userId: String,
    val status: String,
    val isBackup: Int,
    val created: String,
    val updated: String?,
)