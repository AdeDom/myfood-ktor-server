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
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val domainModule = DI.Module(name = "domain") {

    bindSingleton { MyFoodUseCase(instance()) }
    bindSingleton { LoginUseCase(instance()) }
    bindSingleton { RegisterUseCase(instance()) }
    bindSingleton { RefreshTokenUseCase(instance(), instance()) }
    bindSingleton { DeleteAccountUseCase(instance()) }
    bindSingleton { LogoutUseCase(instance()) }
    bindSingleton { UserProfileUseCase(instance()) }
    bindSingleton { ChangeProfileUseCase(instance()) }
    bindSingleton { ChangePasswordUseCase(instance()) }
    bindSingleton { InsertCategoryUseCase(instance()) }
    bindSingleton { InsertFoodUseCase(instance(), instance()) }
    bindSingleton { GetFoodDetailUseCase(instance()) }
    bindSingleton { GetCategoryAllUseCase(instance()) }
    bindSingleton { GetFoodByCategoryIdUseCase(instance(), instance()) }
    bindSingleton { GetFoodAndCategoryGroupAllUseCase(instance(), instance()) }
    bindSingleton { GetFavoriteAllUseCase(instance()) }
    bindSingleton { DeleteFavoriteAllUseCase(instance()) }
    bindSingleton { MyFavoriteUseCase(instance(), instance()) }
    bindSingleton { SyncDataFavoriteUseCase(instance()) }
    bindSingleton { GetRatingScoreAllUseCase(instance()) }
    bindSingleton { DeleteRatingScoreAllUseCase(instance()) }
    bindSingleton { MyRatingScoreUseCase(instance(), instance()) }
    bindSingleton { SyncDataRatingScoreUseCase(instance()) }
    bindSingleton { SyncDataAuthUseCase(instance()) }
    bindSingleton { TokenUseCase(instance()) }
}