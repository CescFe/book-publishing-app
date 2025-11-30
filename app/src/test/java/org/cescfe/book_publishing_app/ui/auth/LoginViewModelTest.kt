package org.cescfe.book_publishing_app.ui.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.domain.auth.model.AuthResult
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.auth.model.ErrorType
import org.cescfe.book_publishing_app.domain.auth.repository.AuthRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var mockRepository: MockAuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockRepository = MockAuthRepository()
        viewModel = LoginViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() {
        val initialState = viewModel.uiState.value

        assertEquals("", initialState.username)
        assertEquals("", initialState.password)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
        assertFalse(initialState.isAuthenticated)
    }

    @Test
    fun `onUsernameChange should update username and clear error`() {
        viewModel.onUsernameChange("test@example.com")

        val state = viewModel.uiState.value
        assertEquals("test@example.com", state.username)
        assertNull(state.error)
    }

    @Test
    fun `onPasswordChange should update password and clear error`() {
        viewModel.onPasswordChange("password123")

        val state = viewModel.uiState.value
        assertEquals("password123", state.password)
        assertNull(state.error)
    }

    @Test
    fun `onLoginClick with empty username should show error`() {
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        val state = viewModel.uiState.value
        assertTrue(state.error != null)
        assertFalse(state.isLoading)
        assertFalse(state.isAuthenticated)
    }

    @Test
    fun `onLoginClick with empty password should show error`() {
        viewModel.onUsernameChange("test@example.com")
        viewModel.onLoginClick()

        val state = viewModel.uiState.value
        assertTrue(state.error != null)
        assertFalse(state.isLoading)
        assertFalse(state.isAuthenticated)
    }

    @Test
    fun `onLoginClick with empty fields should show error`() {
        viewModel.onLoginClick()

        val state = viewModel.uiState.value
        assertTrue(state.error != null)
        assertFalse(state.isLoading)
    }

    @Test
    fun `onLoginClick with valid fields should authenticate successfully`() = runTest {
        mockRepository.result = AuthResult.Success(
            AuthToken(
                accessToken = "test_token",
                tokenType = "Bearer",
                expiresIn = 86400,
                scope = "read write delete",
                userId = "user123"
            )
        )

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.isAuthenticated)
        assertNull(finalState.error)
    }

    @Test
    fun `onLoginClick with invalid credentials should show error`() = runTest {
        mockRepository.result = AuthResult.Error(
            ErrorType.INVALID_CREDENTIALS,
            "Invalid credentials"
        )

        viewModel.onUsernameChange("wrong@example.com")
        viewModel.onPasswordChange("wrongpassword")
        viewModel.onLoginClick()

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertFalse(finalState.isAuthenticated)
        assertEquals("Invalid credentials", finalState.error)
    }

    @Test
    fun `onLoginClick with network error should show error`() = runTest {
        mockRepository.result = AuthResult.Error(
            ErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertFalse(finalState.isAuthenticated)
        assertTrue(finalState.error?.contains("Network") == true)
    }

    @Test
    fun `onLoginClick should clear previous error when starting new login`() = runTest {
        viewModel.onLoginClick()
        val errorState = viewModel.uiState.value
        assertTrue(errorState.error != null)

        mockRepository.result = AuthResult.Success(
            AuthToken(
                accessToken = "test_token",
                tokenType = "Bearer",
                expiresIn = 86400,
                scope = "read",
                userId = "user123"
            )
        )

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertNull(finalState.error)
        assertTrue(finalState.isAuthenticated)
    }
}
class MockAuthRepository : AuthRepository {
    var result: AuthResult = AuthResult.Error("Not configured")

    override suspend fun login(username: String, password: String): AuthResult = result
}
