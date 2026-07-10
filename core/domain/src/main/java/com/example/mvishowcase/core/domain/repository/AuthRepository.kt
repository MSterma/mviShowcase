package com.example.mvishowcase.core.domain.repository

import kotlinx.coroutines.flow.Flow

data class User(val id: String, val email: String?)

interface AuthRepository {
    val authState: Flow<User?>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signOut()
}
