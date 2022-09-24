package com.myfood.server.data.models.entities

import org.joda.time.DateTime

data class FoodEntity(
    val foodId: Int,
    val foodName: String,
    val alias: String?,
    val image: String,
    val price: Double,
    val description: String?,
    val categoryId: Int,
    val status: String,
    val created: DateTime,
    val updated: DateTime?,
)