package com.myfood.server.di

import com.myfood.server.usecase.auth.*
import com.myfood.server.usecase.category.GetCategoryAllUseCase
import com.myfood.server.usecase.category.InsertCategoryUseCase
import com.myfood.server.usecase.favorite.DeleteFavoriteAllUseCase
import com.myfood.server.usecase.favorite.GetFavoriteAllUseCase
import com.myfood.server.usecase.favorite.MyFavoriteUseCase
import com.myfood.server.usecase.favorite.SyncDataFavoriteUseCase
import com.myfood.server.usecase.food.*
import com.myfood.server.usecase.profile.ChangeProfileUseCase
import com.myfood.server.usecase.profile.DeleteAccountUseCase
import com.myfood.server.usecase.profile.UserProfileUseCase
import com.myfood.server.usecase.rating_score.DeleteRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.GetRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.MyRatingScoreUseCase
import com.myfood.server.usecase.rating_score.SyncDataRatingScoreUseCase
import org.koin.dsl.module

val domainModule = module {

    single { MyFoodUseCase(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { RefreshTokenUseCase(get(), get()) }
    single { DeleteAccountUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { UserProfileUseCase(get()) }
    single { ChangeProfileUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { InsertCategoryUseCase(get()) }
    single { InsertFoodUseCase(get(), get()) }
    single { GetFoodDetailUseCase(get()) }
    single { GetCategoryAllUseCase(get()) }
    single { GetFoodByCategoryIdUseCase(get(), get()) }
    single { GetFoodAndCategoryGroupAllUseCase(get(), get()) }
    single { GetFavoriteAllUseCase(get()) }
    single { DeleteFavoriteAllUseCase(get()) }
    single { MyFavoriteUseCase(get(), get()) }
    single { SyncDataFavoriteUseCase(get()) }
    single { GetRatingScoreAllUseCase(get()) }
    single { DeleteRatingScoreAllUseCase(get()) }
    single { MyRatingScoreUseCase(get(), get()) }
    single { SyncDataRatingScoreUseCase(get()) }
    single { SyncDataAuthUseCase(get()) }
    single { TokenUseCase(get()) }
}