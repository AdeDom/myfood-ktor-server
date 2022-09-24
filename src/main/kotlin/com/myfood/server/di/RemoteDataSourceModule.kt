package com.myfood.server.di

import com.myfood.server.data.database.mysql.MySqlDatabase
import com.myfood.server.data.resouce.remote.auth.AuthRemoteDataSource
import com.myfood.server.data.resouce.remote.auth.AuthRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.category.CategoryRemoteDataSource
import com.myfood.server.data.resouce.remote.category.CategoryRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.favorite.FavoriteRemoteDataSource
import com.myfood.server.data.resouce.remote.favorite.FavoriteRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.food.FoodRemoteDataSource
import com.myfood.server.data.resouce.remote.food.FoodRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.food.MyFoodRemoteDataSource
import com.myfood.server.data.resouce.remote.food.MyFoodRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.profile.ProfileRemoteDataSource
import com.myfood.server.data.resouce.remote.profile.ProfileRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.rating_score.RatingScoreRemoteDataSource
import com.myfood.server.data.resouce.remote.rating_score.RatingScoreRemoteDataSourceImpl
import com.myfood.server.data.resouce.remote.user.UserRemoteDataSource
import com.myfood.server.data.resouce.remote.user.UserRemoteDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModule = module {

    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<MyFoodRemoteDataSource> { MyFoodRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<ProfileRemoteDataSource> { ProfileRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<CategoryRemoteDataSource> { CategoryRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<FoodRemoteDataSource> { FoodRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<FavoriteRemoteDataSource> { FavoriteRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
    single<RatingScoreRemoteDataSource> { RatingScoreRemoteDataSourceImpl(get<MySqlDatabase>().getDatabase()) }
}