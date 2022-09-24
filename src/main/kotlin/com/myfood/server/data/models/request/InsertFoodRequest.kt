package com.myfood.server.data.models.request

@kotlinx.serialization.Serializable
data class InsertFoodRequest(
    val foodName: String?,
    val alias: String?,
    val image: String?,
    val price: Double?,
    val description: String?,
    val categoryId: Int?,
)