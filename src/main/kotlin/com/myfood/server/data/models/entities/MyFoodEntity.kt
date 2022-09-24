package com.myfood.server.data.models.entities

@kotlinx.serialization.Serializable
data class MyFoodEntity(
    val id: Int,
    val foodDefault: String,
)