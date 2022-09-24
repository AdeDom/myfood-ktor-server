package com.myfood.server.data.models.request

@kotlinx.serialization.Serializable
data class ChangeProfileRequest(
    val name: String?,
    val mobileNo: String?,
    val address: String?,
)