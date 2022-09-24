package com.myfood.server.data.models.request

@kotlinx.serialization.Serializable
data class ChangePasswordRequest(
    val oldPassword: String?,
    val newPassword: String?,
)