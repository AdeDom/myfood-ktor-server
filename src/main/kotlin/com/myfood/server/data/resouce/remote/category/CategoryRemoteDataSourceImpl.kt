package com.myfood.server.data.resouce.remote.category

import com.myfood.server.data.database.mysql.CategoryTable
import com.myfood.server.data.models.entities.CategoryEntity
import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.utility.constant.AppConstant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime

internal class CategoryRemoteDataSourceImpl(
    private val db: Database,
) : CategoryRemoteDataSource {

    override suspend fun findCategoryId(categoryId: Int): Long {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            CategoryTable
                .select {
                    CategoryTable.categoryId eq categoryId
                }
                .count()
        }
    }

    override suspend fun insertCategory(insertCategoryRequest: InsertCategoryRequest): Int? {
        val (categoryName, image) = insertCategoryRequest

        val statement = newSuspendedTransaction(Dispatchers.IO, db) {
            CategoryTable.insert {
                it[CategoryTable.categoryName] = categoryName!!
                it[CategoryTable.image] = image!!
                it[created] = DateTime(System.currentTimeMillis() + AppConstant.DATE_TIME_THAI)
            }
        }

        return statement.resultedValues?.size
    }

    override suspend fun getCategoryAll(): List<CategoryEntity> {
        return newSuspendedTransaction(Dispatchers.IO, db) {
            CategoryTable
                .slice(
                    CategoryTable.categoryId,
                    CategoryTable.categoryName,
                    CategoryTable.image,
                    CategoryTable.categoryTypeName,
                    CategoryTable.created,
                    CategoryTable.updated,
                )
                .selectAll()
                .map { row ->
                    CategoryEntity(
                        categoryId = row[CategoryTable.categoryId],
                        categoryName = row[CategoryTable.categoryName],
                        image = row[CategoryTable.image],
                        categoryTypeName = row[CategoryTable.categoryTypeName],
                        created = row[CategoryTable.created],
                        updated = row[CategoryTable.updated],
                    )
                }
        }
    }
}