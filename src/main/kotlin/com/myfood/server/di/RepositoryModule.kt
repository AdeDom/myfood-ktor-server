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
import org.koin.dsl.module

val repositoryModule = module {

    single<FoodRepository> { FoodRepositoryImpl(get(), get(), get(), get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get(), get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get(), get()) }
    single<RatingScoreRepository> { RatingScoreRepositoryImpl(get(), get()) }
}