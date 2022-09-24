package com.myfood.server.data.models.request

@kotlinx.serialization.Serializable
data class RegisterRequest(
    val email: String?,
    val password: String?,
    val name: String?,
    val mobileNo: String?,
    val address: String?,
)