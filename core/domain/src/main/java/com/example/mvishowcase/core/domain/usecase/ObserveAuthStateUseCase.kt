package com.example.mvishowcase.core.domain.usecase

import com.example.mvishowcase.core.domain.repository.AuthRepository
import com.example.mvishowcase.core.domain.repository.User
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<User?> = authRepository.authState
}
