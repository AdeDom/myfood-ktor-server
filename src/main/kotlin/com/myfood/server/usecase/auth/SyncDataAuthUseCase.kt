package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository

class SyncDataAuthUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): Resource<BaseResponse<String>> {
        return when {
            else -> {
                authRepository.syncDataAuth()
            }
        }
    }
}