package com.myfood.server.di

import com.myfood.server.data.database.sqlite.SqliteDatabase
import com.myfood.server.data.resouce.local.auth.AuthLocalDataSource
import com.myfood.server.data.resouce.local.auth.AuthLocalDataSourceImpl
import com.myfood.server.data.resouce.local.category.CategoryLocalDataSource
import com.myfood.server.data.resouce.local.category.CategoryLocalDataSourceImpl
import com.myfood.server.data.resouce.local.favorite.FavoriteLocalDataSource
import com.myfood.server.data.resouce.local.favorite.FavoriteLocalDataSourceImpl
import com.myfood.server.data.resouce.local.food.FoodLocalDataSource
import com.myfood.server.data.resouce.local.food.FoodLocalDataSourceImpl
import com.myfood.server.data.resouce.local.food_and_category.FoodAndCategoryLocalDataSource
import com.myfood.server.data.resouce.local.food_and_category.FoodAndCategoryLocalDataSourceImpl
import com.myfood.server.data.resouce.local.rating_score.RatingScoreLocalDataSource
import com.myfood.server.data.resouce.local.rating_score.RatingScoreLocalDataSourceImpl
import com.myfood.server.data.resouce.local.user.UserLocalDataSource
import com.myfood.server.data.resouce.local.user.UserLocalDataSourceImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val localDataSourceModule = DI.Module(name = "local_data_source") {

    bindSingleton<FavoriteLocalDataSource> { FavoriteLocalDataSourceImpl(instance<SqliteDatabase>().getDatabase()) }
    bindSingleton<RatingScoreLocalDataSource> { RatingScoreLocalDataSourceImpl(instance<SqliteDatabase>().getDatabase()) }
    bindSingleton<AuthLocalDataSource> { AuthLocalDataSourceImpl(instance<SqliteDatabase>().getDatabase()) }

    bindSingleton<UserLocalDataSource> { UserLocalDataSourceImpl() }
    bindSingleton<CategoryLocalDataSource> { CategoryLocalDataSourceImpl() }
    bindSingleton<FoodLocalDataSource> { FoodLocalDataSourceImpl() }
    bindSingleton<FoodAndCategoryLocalDataSource> { FoodAndCategoryLocalDataSourceImpl() }
}