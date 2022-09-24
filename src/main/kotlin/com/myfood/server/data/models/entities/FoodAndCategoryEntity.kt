package com.myfood.server.data.models.entities

import org.joda.time.DateTime

data class FoodAndCategoryEntity(
    val foodAndCategoryId: Int,
    val foodId: Int?,
    val foodName: String?,
    val alias: String?,
    val foodImage: String?,
    val price: Double?,
    val description: String?,
    val status: String?,
    val foodCreated: DateTime?,
    val foodUpdated: DateTime?,
    val categoryId: Int,
    val categoryName: String,
    val categoryImage: String,
    val categoryTypeName: String,
    val categoryCreated: DateTime,
    val categoryUpdated: DateTime?,
)