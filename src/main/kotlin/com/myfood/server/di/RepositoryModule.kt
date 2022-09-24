package com.myfood.server.di

import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.data.repositories.auth.AuthRepositoryImpl
import com.myfood.server.data.repositories.category.CategoryRepository
import com.myfood.server.data.repositories.category.CategoryRepositoryImpl
import com.myfood.server.data.repositories.favorite.FavoriteRepository
import com.myfood.server.data.repositories.favorite.FavoriteRepositoryImpl
import com.myfood.server.data.repositories.food.FoodRepository
import com.myfood.server.data.repositories.food.FoodRepositoryImpl
import com.myfood.server.data.repositories.profile.ProfileRepository
import com.myfood.server.data.repositories.profile.ProfileRepositoryImpl
import com.myfood.server.data.repositories.rating_score.RatingScoreRepository
import com.myfood.server.data.repositories.rating_score.RatingScoreRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val repositoryModule = DI.Module(name = "repository") {

    bindSingleton<FoodRepository> {
        FoodRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }
    bindSingleton<AuthRepository> { AuthRepositoryImpl(instance(), instance(), instance()) }
    bindSingleton<ProfileRepository> {
        ProfileRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }
    bindSingleton<CategoryRepository> { CategoryRepositoryImpl(instance(), instance()) }
    bindSingleton<FavoriteRepository> { FavoriteRepositoryImpl(instance(), instance()) }
    bindSingleton<RatingScoreRepository> { RatingScoreRepositoryImpl(instance(), instance()) }
}