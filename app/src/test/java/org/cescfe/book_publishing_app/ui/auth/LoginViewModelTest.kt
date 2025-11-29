package org.cescfe.book_publishing_app.ui.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = LoginViewModel()
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
    fun `onLoginClick with valid fields should set loading state`() = runTest {
        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertNull(loadingState.error)
    }

    @Test
    fun `onLoginClick with valid fields should authenticate after delay`() = runTest {
        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceTimeBy(1500L)
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.isAuthenticated)
        assertNull(finalState.error)
    }

    @Test
    fun `onLoginClick should clear previous error when starting new login`() = runTest {
        viewModel.onLoginClick()
        val errorState = viewModel.uiState.value
        assertTrue(errorState.error != null)

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        val loadingState = viewModel.uiState.value
        assertNull(loadingState.error)
        assertTrue(loadingState.isLoading)
    }
}