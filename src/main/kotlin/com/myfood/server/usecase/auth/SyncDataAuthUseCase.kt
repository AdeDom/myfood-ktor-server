package com.myfood.server.usecase.auth

import com.myfood.server.data.repositories.auth.AuthRepository

internal class SyncDataAuthUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): String {
        return authRepository.syncDataAuth()
    }
}