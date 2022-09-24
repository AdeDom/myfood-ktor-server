package com.myfood.server.data.models.entities

import org.joda.time.DateTime

internal data class CategoryEntity(
    val categoryId: Int,
    val categoryName: String,
    val image: String,
    val categoryTypeName: String,
    val created: DateTime,
    val updated: DateTime?,
)