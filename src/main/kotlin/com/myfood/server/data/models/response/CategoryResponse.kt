package com.myfood.server.data.models.response

@kotlinx.serialization.Serializable
data class CategoryResponse(
    val categoryId: Int,
    val categoryName: String,
    val image: String,
    val categoryTypeName: String,
    val created: String,
    val updated: String?,
)