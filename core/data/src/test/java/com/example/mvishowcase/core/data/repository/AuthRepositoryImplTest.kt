package com.example.mvishowcase.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private val firebaseAuth: FirebaseAuth = mockk(relaxed = true)
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(firebaseAuth)
    }

    @Test
    fun `signOut should call firebaseAuth signOut`() = runTest {
        repository.signOut()
        verify { firebaseAuth.signOut() }
    }
}
