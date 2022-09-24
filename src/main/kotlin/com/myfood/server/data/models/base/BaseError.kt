package com.myfood.server.data.models.base

@kotlinx.serialization.Serializable
data class BaseError(
    val code: String? = null,
    val message: String? = null,
)