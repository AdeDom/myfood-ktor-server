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
import org.koin.dsl.module

val localDataSourceModule = module {

    single<FavoriteLocalDataSource> { FavoriteLocalDataSourceImpl(get<SqliteDatabase>().getDatabase()) }
    single<RatingScoreLocalDataSource> { RatingScoreLocalDataSourceImpl(get<SqliteDatabase>().getDatabase()) }
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(get<SqliteDatabase>().getDatabase()) }

    single<UserLocalDataSource> { UserLocalDataSourceImpl() }
    single<CategoryLocalDataSource> { CategoryLocalDataSourceImpl() }
    single<FoodLocalDataSource> { FoodLocalDataSourceImpl() }
    single<FoodAndCategoryLocalDataSource> { FoodAndCategoryLocalDataSourceImpl() }
}