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
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val remoteDataSourceModule = DI.Module(name = "remote_data_source") {

    bindSingleton<UserRemoteDataSource> { UserRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<MyFoodRemoteDataSource> { MyFoodRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<ProfileRemoteDataSource> { ProfileRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<CategoryRemoteDataSource> { CategoryRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<FoodRemoteDataSource> { FoodRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<FavoriteRemoteDataSource> { FavoriteRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
    bindSingleton<RatingScoreRemoteDataSource> { RatingScoreRemoteDataSourceImpl(instance<MySqlDatabase>().getDatabase()) }
}